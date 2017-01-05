package uk.co.seanhodges.importer.writer

import com.google.cloud.datastore._

/**
 * Created by sean on 19/12/16.
 */
class GAEDatastoreWriter extends ContentWriter {

  var datastore : Datastore = DatastoreOptions.getDefaultInstance.getService

  def calculateNextRef(refPrefix : String) : String = {
    val kind = "sh_refcounter"
    val refKey = datastore.newKeyFactory().setKind(kind).newKey(refPrefix)
    val entity : Entity = datastore.get(refKey, ReadOption.eventualConsistency())
    val currentVal = entity.getLong("lastIndex")

    val result : Entity = Entity.newBuilder(entity)
      .set("lastIndex", currentVal + 1)
      .build()
    datastore.update(result)

    refPrefix + "_" + result.getLong("lastIndex") // Return the new index
  }

  override def put(articleData : ArticleMap): Entity = {
    val kind = "sh_article"
    val nextRefId = calculateNextRef("SH_TEST")

    val taskKey = datastore.newKeyFactory().setKind(kind).newKey(nextRefId)

    // Prepares the new entity using the contents of the article map
    val builder = Entity.newBuilder(taskKey)
    for ((key, value) <- articleData) {
      builder.set(key, value)
    }
    builder.set("dateCreated", DateTime.now)
    builder.set("dateModified", DateTime.now)
    val task : Entity = builder.build()

    // Saves the entity
    datastore.put(task)
  }

  override def close: Any = ()
}
