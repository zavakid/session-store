import sbt._
import Keys._
import org.sbtidea.SbtIdeaPlugin._
import xerial.sbt.Sonatype.SonatypeKeys._

object SessionStoreBuild extends Build {
  import BuildSettings._
  import Dependencies._

  lazy val core = Project(id = "sessionStore", base = file("."))
    .settings(coreSettings: _*)
    .settings(libraryDependencies ++=
      coreDependencies)
    .settings(xerial.sbt.Sonatype.sonatypeSettings: _*)
}

object BuildSettings {
  lazy val ScalaVersion = "2.11.2"

  lazy val basicSettings = seq(
    organization          := "com.zavakid.sessionstore",
    startYear             := Some(2014),
    licenses              := Seq("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    scalaVersion          := ScalaVersion,
    ideaExcludeFolders := ".idea" :: ".idea_modules" :: Nil,
    // sbt-idea: donot download javadoc, we donot like them
    transitiveClassifiers in Global := Seq(Artifact.SourceClassifier),
    testOptions in Test := Seq(Tests.Filter(s => s.endsWith("Test") || s.endsWith("Spec"))),
    scalacOptions := Seq(
      "-deprecation",
      "-unchecked",
      "-encoding", "UTF-8",
      "-target:jvm-1.6",
      "-Xlint",
      "-Yclosure-elim",
      "-Yinline",
      "-feature",
      "-language:postfixOps",
      "-optimise"   // this option will slow our build
      ),
    javacOptions := Seq(
      "-target", "1.6" ,
      "-source", "1.6",
      "-Xlint:unchecked"
      //"-Xlint:deprecation"
      ),
    //externalResolvers <<= resolvers map { rs =>
    //  Resolver.withDefaultResolvers(rs, mavenCentral = false)
    //},
    resolvers ++= Seq(
        "99-empty" at "http://version99.qos.ch/"
      )
    ,pomExtra := {
      <url>http://www.zavakid.com/sbt-one-log/</url>
        <scm>
          <connection>scm:git:github.com:zavakid/session-store.git</connection>
          <developerConnection>scm:git:git@github.com:zavakid/session-store.git</developerConnection>
          <url>github.com/zavakid/session-store</url>
        </scm>
        <developers>
          <developer>
            <id>zava</id>
            <name>Zavakid</name>
            <url>http://www.zavakid.com</url>
          </developer>
        </developers>
    }
    ) ++ net.virtualvoid.sbt.graph.Plugin.graphSettings
    

    lazy val coreSettings = basicSettings

}

object Dependencies {

  def basicDependencies:Seq[ModuleID] = Seq(
      //"com.typesafe" % "config" % "1.2.1",
      //"com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
    )

  def coreDependencies:Seq[ModuleID] = basicDependencies ++ Seq(
      //"io.netty" % "netty-all" % "4.0.23.Final"
      //,"com.typesafe.akka" % "akka-actor_2.11" % "2.3.5"
      //,"com.typesafe.akka" % "akka-slf4j_2.11" % "2.3.5"
      "javax.servlet" % "servlet-api" % "2.5"
      ,"com.alibaba.citrus" % "citrus-webx-all" % "3.2.4"

      //test
      ,"org.eclipse.jetty" % "jetty-webapp" % "7.6.15.v20140411" % "test"
  )

}
