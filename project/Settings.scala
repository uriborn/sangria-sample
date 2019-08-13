import sbt.Keys._
import sbt._
import sbtassembly.AssemblyKeys._
import sbtassembly.AssemblyPlugin.autoImport.{ assembly => _, assemblyMergeStrategy => _ }

import scala.collection.JavaConverters._

object Settings {

  lazy val scala212Version = "2.12.8"

  lazy val basicSettings = Seq(
    organization := "influencer",
    scalaVersion in ThisBuild := scala212Version,
    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-feature",
      "-deprecation",
      "-unchecked",
      "-language:_",
      "-Xmax-classfile-name", "242",
      "-Ypartial-unification",
      "-target:jvm-1.8"
    ),
    fork := true,
    javaOptions in GlobalScope ++= Seq(
      "-Duser.timezone=UTC"
    ),
    test in assembly := {},
    sources in doc in Compile := Seq.empty,
    sources in doc in update := Seq.empty,
    sources in doc in run := Seq.empty
  )

  lazy val DebugTest = config("debug") extend Test
  lazy val MySQLTest = config("mysql") extend Test
  lazy val TestSeq   = Seq(DebugTest, MySQLTest)

  lazy val javaVmArguments = java.lang.management.ManagementFactory.getRuntimeMXBean.getInputArguments.asScala.toList

  lazy val basicTestSettings = Seq(
    javaOptions in Test ++= javaVmArguments.filter(a => Seq("-Xmx", "-Xms", "-XX").exists(a.startsWith)),
    testOptions in Test += Tests.Argument("-oT"),
    testOptions in Test += Tests.Argument(s"-Duser.timezone=UTC"),
    fork in DebugTest := true
  ) ++
  inConfig(DebugTest)(Defaults.testTasks ++ Seq(forkOptions := Defaults.forkOptionsTask.value)) ++
  inConfig(MySQLTest)(Defaults.testTasks ++ Seq(forkOptions := Defaults.forkOptionsTask.value))
}
