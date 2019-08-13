import sbt._

object Dependencies {

  val logback             = "ch.qos.logback"               %  "logback-classic"     % "1.2.3"
  val sangria             = "org.sangria-graphql"          %% "sangria"             % "1.4.2"
  val sangriaCirce        = "org.sangria-graphql"          %% "sangria-circe"       % "1.2.1"
  val sangriaSlowLog      = "org.sangria-graphql"          %% "sangria-slowlog"     % "0.1.8"
  val awsLambda           = "com.amazonaws"                %  "aws-java-sdk-lambda" % "1.11.592"
  val guice               = "com.google.inject"            % "guice"                % "4.2.2"
  val guiceAssistedInject = "com.google.inject.extensions" % "guice-assistedinject" % "4.2.2"
  val scalatest           = "org.scalatest"                %% "scalatest"           % "3.0.8"

  sealed trait BaseProject {
    lazy val dependencies = compileDependencies ++ testDependencies

    val compileDependencies: Seq[ModuleID]
    val testDependencies: Seq[ModuleID]

    def compile(deps: ModuleID*): Seq[ModuleID] = deps map (_ % Compile) map (_.exclude("commons-logging", "commons-logging"))
    def test(deps: ModuleID*): Seq[ModuleID] = deps map (_ % Test)
  }

  object SangriaProject extends BaseProject {
    val compileDependencies = compile(logback, sangria, sangriaCirce, sangriaSlowLog, awsLambda, guice, guiceAssistedInject)
    val testDependencies = test(scalatest)
  }

}
