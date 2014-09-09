package com.zavakid.requestcontext

import javax.servlet._
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.alibaba.citrus.service.requestcontext.lazycommit.LazyCommitRequestContext
import com.alibaba.citrus.service.requestcontext.util.RequestContextUtil
import com.alibaba.citrus.service.requestcontext.{RequestContext, RequestContextChainingService}
import com.alibaba.citrus.service.resource.support.context.ResourceLoadingXmlWebApplicationContext

/**
 *
 * @author zebin.xuzb 2014-09-08
 */
class RequestContextFilter extends Filter {

  import com.zavakid.requestcontext.RequestContextFilter._

  var rcChainingService: RequestContextChainingService = _
  var sc: ServletContext = _

  override def init(filterConfig: FilterConfig): Unit = {
    sc = filterConfig.getServletContext
    rcChainingService = createRequestContextService(filterConfig)
  }

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = {
    val rc = getRequestContext(request.asInstanceOf[HttpServletRequest]).getOrElse {
      rcChainingService.getRequestContext(sc, request.asInstanceOf[HttpServletRequest], response.asInstanceOf[HttpServletResponse])
    }

    if (!isRequestFinished(rc)) try {
      val req = rc.getRequest
      val rsp = rc.getResponse
      chain.doFilter(req, rsp)
    } finally {
      rcChainingService.commitRequestContext(rc)
    }
  }

  override def destroy(): Unit = {}
}

object RequestContextFilter {
  //  val KEY = "__RequestContextFilter__"
  val CONFIG_KEY = "config"

  val SPRING_CONTEXT_KEY = "__SPRINT_CONTEXT__KEY__"
  val RC_KEY = "__RC_KEY__"

  def getRequestContext(request: HttpServletRequest): Option[RequestContext] =
    Option(RequestContextUtil.getRequestContext(request))

  def isRequestFinished(rc: RequestContext): Boolean =
    Option(RequestContextUtil.findRequestContext(rc, classOf[LazyCommitRequestContext])).exists(_.isRedirected)

  def createRequestContextService(config: FilterConfig): RequestContextChainingService = {
    val configPath = config.getInitParameter(CONFIG_KEY)
    val springContext = new ResourceLoadingXmlWebApplicationContext()
    springContext.setConfigLocation(configPath)
    springContext.setServletContext(config.getServletContext)
    springContext.refresh()
    val rcChainingService = springContext.getBean(classOf[RequestContextChainingService])
    assert(rcChainingService != null)
    config.getServletContext.setAttribute(SPRING_CONTEXT_KEY, springContext)
    config.getServletContext.setAttribute(RC_KEY, rcChainingService)
    rcChainingService
  }
}
