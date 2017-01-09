name := "DatastoreBackup"

version := "1.0"

scalaVersion := "2.11.8"

mainClass in Compile := Some("uk.co.seanhodges.importer.Main")

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
//resolvers += "Typesafe repository" at "https://mvnrepository.com/artifact/com.google.appengine/appengine-api-1.0-sdk"

libraryDependencies ++= Seq(
  "org.scalesxml" %% "scales-xml" % "0.6.0-M2",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.slf4j" % "slf4j-simple" % "1.7.22",
  "joda-time" % "joda-time" % "2.9.7",
  "com.google.appengine" % "appengine-api-1.0-sdk" % "1.9.48",
  "com.google.appengine" % "appengine-testing" % "1.9.48",
  "com.google.appengine" % "appengine-api-stubs" % "1.9.48"
)



// The new logback implementation is Scala 2.11 only...
//libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"
//libraryDependencies += "com.typesafe.scalalogging" %% "scalalogging" % "1.1.0"