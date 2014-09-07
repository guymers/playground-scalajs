import sbt._
import Keys._
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import scala.scalajs.sbtplugin.ScalaJSPlugin.ScalaJSKeys._
import com.lihaoyi.workbench.Plugin._

object build extends Build {

  val projectName = "playground-scalajs"

  lazy val baseSettings = Seq(
    name := projectName,
    organization := "me.guymer",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.2",
    scalacOptions := Seq("-unchecked", "-deprecation", "-feature")
  )

  lazy val project = Project(projectName, file("."))
    .settings(baseSettings: _*)
    .settings(scalaJSSettings: _*)
    .settings(
      persistLauncher := true,
      persistLauncher in Test := false,

      jsDependencies += "org.webjars" % "react" % "0.11.1" % "compile" / "react-with-addons.js" commonJSName "React",
      skip in packageJSDependencies := false
    )
    .settings(workbenchSettings: _*)
    .settings(bootSnippet := "JsMain().main();")
    .settings(
      resolvers += "bintray/non" at "http://dl.bintray.com/non/maven",
      libraryDependencies ++= Seq(
        "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6",
        "org.scala-lang.modules.scalajs" %% "scalajs-jasmine-test-framework" % scalaJSVersion % "test",

        //"com.lihaoyi" %% "upickle" % "0.2.3",
        "com.github.japgolly.scalajs-react" %%% "core" % "0.4.0",
        "com.github.japgolly.scalajs-react" %%% "ext-scalaz71" % "0.4.0"
      )
    )

}
