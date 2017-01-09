package uk.co.seanhodges.importer

import org.slf4j.LoggerFactory
import uk.co.seanhodges.importer.consumer.{BackupXMLContentConsumer, DatastoreContentConsumer, WPXMLContentConsumer}
import uk.co.seanhodges.importer.parser.{DatastoreParser, XMLParser}
import uk.co.seanhodges.importer.writer.{XMLContentWriter, DatastoreWriter}

/**
 * Created by sean on 19/12/16.
 */
trait ModuleInjection {

  object BackupDeps {
    lazy val parser = new DatastoreParser()
    lazy val writer = new XMLContentWriter()
  }
  lazy val backupDS = new DatastoreContentConsumer(BackupDeps.parser, BackupDeps.writer)

  object RestoreDeps {
    lazy val parser = new XMLParser()
    lazy val writer = new DatastoreWriter()
  }
  lazy val restoreWP = new WPXMLContentConsumer(RestoreDeps.parser, RestoreDeps.writer)
  lazy val restoreDS = new BackupXMLContentConsumer(RestoreDeps.parser, RestoreDeps.writer)

}
