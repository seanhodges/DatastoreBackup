package uk.co.seanhodges.importer.writer

import com.google.appengine.api.datastore._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.slf4j.LoggerFactory

/**
 * Created by sean on 19/12/16.
 */
class DatastoreWriter extends ContentWriter {
  private val logger = LoggerFactory.getLogger(getClass.getName)

  val datastore: DatastoreService = DatastoreServiceFactory.getDatastoreService

  def calculateNextRef(refPrefix : String) : String = {
    val kind = "sh_refcounter"

    val refKey: Key = KeyFactory.createKey(kind, refPrefix)
    val entity : Entity = datastore.get(refKey)
    val currentVal: Long = entity.getProperty("lastIndex").toString.toLong

    entity.setProperty("lastIndex", currentVal + 1)
    datastore.put(entity)
    refPrefix + "_" + (currentVal + 1) // Return the new index
  }

  override def put(articleData : ArticleMap): Entity = {
    val kind = "sh_article"
    val nextRefId = calculateNextRef("SH_TEST")

    val entity: Entity = new Entity(kind, nextRefId)
    for ((key, value) <- articleData) {
      entity.setProperty(key, value)
    }
    entity.setProperty("dateCreated", DateTime.now.toDate)
    entity.setProperty("dateModified", DateTime.now.toDate)

    // Saves the entity
    logger.info(s"Storing database entry for $nextRefId to $kind")
    this.datastore.put(entity)
    entity
  }

  override def close: Any = ()
}
