package uk.co.seanhodges.importer

import org.slf4j.{Logger, LoggerFactory}

import scala.io.Source
import scala.reflect.io.File
import scala.xml.pull.XMLEventReader

/**
 * Created by sean on 16/12/16.
 */
object Main extends App with ModuleInjection {
  private val logger = LoggerFactory.getLogger(getClass.getName)

  if (args.isEmpty) {
    val program = new Exception().getStackTrace.head.getFileName
    logger.info(s"Please support a target XML file: $program [FILENAME]")
    logger.info(s"")
    System.exit(1)
  }
  if (!File(args(0)).exists) {
    val filePath = args(0)
    logger.info(s"Target XML file $filePath does not exist")
    System.exit(1)
  }

  val xml = new XMLEventReader(Source.fromFile(args(0), "UTF-8"))
  consumer.start(xml).end

  logger.info(s"Import finished successfully")
}