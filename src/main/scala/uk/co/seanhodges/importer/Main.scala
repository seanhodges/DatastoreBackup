package uk.co.seanhodges.importer

import scala.io.Source
import scala.reflect.io.File
import scala.xml.pull.XMLEventReader

/**
 * Created by sean on 16/12/16.
 */
object Main extends App with ModuleInjection {
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
  consumer.start(xml).end

  println("Import finished successfully")
}