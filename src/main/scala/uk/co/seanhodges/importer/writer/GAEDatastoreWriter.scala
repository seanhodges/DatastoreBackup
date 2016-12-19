package uk.co.seanhodges.importer.writer

import com.google.cloud.datastore.{DatastoreOptions, Datastore, DateTime, Entity}

/**
 * Created by sean on 19/12/16.
 */
class GAEDatastoreWriter {

  var datastore : Datastore = DatastoreOptions.getDefaultInstance.getService

  def put(articleData : Map[String, String]) = {
    val kind = "sitecore_article"
    val nextRefId = "SH_TEST_000001"

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
}
