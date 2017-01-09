package uk.co.seanhodges.importer.writer

/**
 * Created by sean on 16/12/16.
 */

import java.util.Date

import com.google.appengine.api.datastore.{DatastoreService, DatastoreServiceFactory, Entity}
import com.google.appengine.tools.development.testing.{LocalDatastoreServiceTestConfig, LocalServiceTestHelper}
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.immutable.HashMap

class DatastoreWriterTests extends FunSuite with BeforeAndAfter {

  private val helper: LocalServiceTestHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig)
  val datastore: DatastoreService = DatastoreServiceFactory.getDatastoreService

  before {
    helper.setUp()

    val refCounter: Entity = new Entity("sh_refcounter", "SH_TEST")
    refCounter.setProperty("lastIndex", 1)
    this.datastore.put(refCounter)
  }

  after {
    helper.tearDown()
  }

  test("writes an article to the datastore") {

    // Prepares the new entity
    var articleData = new HashMap[String, String]()
    articleData += ("heading" -> "My test article",
                    "body"    -> "This is a test article",
                    "section" -> "test")

    val writer = new DatastoreWriter()
    val result : Entity = writer.put(articleData)

    assert(result.getKey.isComplete === true)
    assert(result.getProperty("heading").toString == "My test article")
  }
}
