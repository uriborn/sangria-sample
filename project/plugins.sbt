resolvers += "Flyway" at "https://flywaydb.org/repo"
resolvers += "Sonatype OSS Release Repository" at "https://oss.sonatype.org/content/repositories/releases/"
resolvers += "Seasar2 Repository" at "http://maven.seasar.org/maven2"

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.4.1")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.0.2")
addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "5.2.0")
addSbtPlugin("jp.co.septeni-original" % "sbt-dao-generator" % "1.0.8")
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.2")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.0")

libraryDependencies ++= Seq(
  "org.seasar.util" % "s2util" % "0.0.1"
)
