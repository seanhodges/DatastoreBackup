name := "DatastoreBackup"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalesxml" %% "scales-xml" % "0.6.0-M2",
  "com.google.cloud" % "google-cloud-datastore" % "0.8.0-beta",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.22"

// The new logback implementation is Scala 2.11 only...
//libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"
//libraryDependencies += "com.typesafe.scalalogging" %% "scalalogging" % "1.1.0"