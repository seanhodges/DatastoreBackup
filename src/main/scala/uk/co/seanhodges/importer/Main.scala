package uk.co.seanhodges.importer

import uk.co.seanhodges.importer.parser.WPXMLImporter

import scala.io.Source
import scala.reflect.io.File
import scala.xml.pull.XMLEventReader

/**
 * Created by sean on 16/12/16.
 */

object Main extends App {
  if (args.isEmpty) {
    val program = new Exception().getStackTrace.head.getFileName
    println("Please support a target XML file: " + program + " [FILENAME]")
    System.exit(1)
  }
  if (!File(args(0)).exists) {
    println("Target XML file " + args(0) + "does not exist")
    System.exit(1)
  }

  val xml = new XMLEventReader(Source.fromFile(args(0), "UTF-8"))
  val importer = new WPXMLImporter()
  importer.articleListener = None // TODO: Add a collector here
  importer.parse(xml)

  println("Import finished successfully")
}