package uk.co.seanhodges.importer

import org.slf4j.LoggerFactory
import uk.co.seanhodges.importer.consumer.WPContentConsumer
import uk.co.seanhodges.importer.parser.WPXMLParser
import uk.co.seanhodges.importer.writer.GAEDatastoreWriter

/**
 * Created by sean on 19/12/16.
 */
trait ModuleInjection {

  // Use of lazy to avoid instantiation if the module is never used

  lazy val parser = new WPXMLParser()
  lazy val writer = new GAEDatastoreWriter()
  lazy val consumer = new WPContentConsumer(parser, writer)

}
