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
    // Run a Datastore backup
    backupDS.start().end
  }
  else if (File(args(0)).exists) {
    val xml = new XMLEventReader(Source.fromFile(args(0), "UTF-8"))

    // Restore a given backup file
    // TODO: Read ahead and determine backup type from contents
    if (args(0).contains("datastore-backup")) {
      // Restore a datastore backup
      restoreDS.start(xml).end
    }
    else if (args(0).contains("wordpress")) {
      // Restore a wordpress backup
      restoreWP.start(xml).end
    }
    else {
      val filePath = args(0)
      logger.info(s"Unknown backup format")
      System.exit(1)
    }
  }
  else {
    // Bad path provided
    val filePath = args(0)
    logger.info(s"Target XML file $filePath does not exist")
    System.exit(1)
  }

  logger.info(s"Import finished successfully")
}