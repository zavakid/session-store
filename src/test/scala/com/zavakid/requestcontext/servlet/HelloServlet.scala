package com.zavakid.requestcontext.servlet

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

class HelloServlet extends HttpServlet {

  override
  def doGet(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    val session = req.getSession(true)
    Option(session.getAttribute("s1"))
      .map(_.asInstanceOf[SessionContent])
      .fold {
      val sc = new SessionContent
      sc.string = "x"
      sc.int = sc.int + 1
      session.setAttribute("s1", sc)
      resp.getWriter.write("from memory" + sc.string + " " + sc.int)
    } { sc =>
      resp.getWriter.write("from session" + sc.string + " " + sc.int)
    }

  }
}

class SessionContent extends Serializable{
  var string: String = _
  var int: Integer = 0
}
