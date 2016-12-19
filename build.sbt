name := "WPtoDatastoreImporter"

version := "1.0"

libraryDependencies ++= Seq(
  "org.scalesxml" %% "scales-xml" % "0.5.0",
  "com.google.cloud" % "google-cloud-datastore" % "0.8.0-beta",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

//addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.0")