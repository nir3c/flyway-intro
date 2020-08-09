name := "flyway-intro"

version := "0.1"

scalaVersion := "2.13.3"



libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "42.2.14",
  "org.flywaydb" % "flyway-core" % "6.5.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)