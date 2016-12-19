package uk.co.seanhodges.importer.writer

/**
 * Created by sean on 16/12/16.
 */

import org.scalatest.{BeforeAndAfter, FunSuite}

import com.google.cloud.datastore._

import scala.collection.immutable.HashMap

class GAEDatastoreWriterTests extends FunSuite with BeforeAndAfter {

  System.setProperty("DATASTORE_EMULATOR_HOST", "localhost:8040")
  System.setProperty("DATASTORE_APP_ID", "dev~sean-hodges-website")
  System.setProperty("DATASTORE_PROJECT_ID", "sean-hodges-website")

  var datastore : Datastore = DatastoreOptions.getDefaultInstance.getService

  before {
  }

  test("writes an article to the datastore") {

    // Prepares the new entity
    var articleData = new HashMap[String, String]()
    articleData += ("heading" -> "My test article",
                    "body"    -> "This is a test article",
                    "section" -> "test")

    val writer = new GAEDatastoreWriter()
    val result : Entity = writer.put(articleData)

    assert(result.hasKey === true)
    assert(result.getString("heading") == "My test article")
  }

  // mark that you want a test here in the future
  test("test datastore writing")(pending)
}
