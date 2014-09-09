package com.zavakid.requestcontext

import java.io.File

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext

object JettyServer extends App{

  System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.JavaUtilLog")

  val testClassesURL = Thread.currentThread().getContextClassLoader().getResource("")
  assert(testClassesURL != null)


  val webappDir = new File(testClassesURL.getFile() + "testWeb")
  assert(webappDir.isDirectory)


  val server = new Server(8080)
  val context = new WebAppContext()
  context.setResourceBase(webappDir.getAbsolutePath)
  context.setContextPath("/")
  context.setParentLoaderPriority(true)
  server.setHandler(context)
  server.start()
  server.join()
}
