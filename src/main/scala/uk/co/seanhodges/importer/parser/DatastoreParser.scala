package uk.co.seanhodges.importer.parser

import com.google.appengine.api.datastore.Entity
import org.slf4j.LoggerFactory

/**
  * Created by sean on 16/12/16.
  */
class DatastoreParser extends ContentParser[Entity] {
  private val logger = LoggerFactory.getLogger(getClass.getName)

  private def processData(ent: Entity) {
    val key = ent.getKey
    logger.debug(s"Processing entity $key")
    this.articleData += (
      "ref" -> ent.getKey.getName,
      "heading" -> ent.getProperty("heading").toString,
      "body" -> ent.getProperty("body").toString
    )
  }

  def parse(xml: Iterator[Entity]): Unit = {
    while (xml.hasNext) {
      val ent: Entity = xml.next
      this.processData(ent)
      sendArticle()
    }
  }

}
