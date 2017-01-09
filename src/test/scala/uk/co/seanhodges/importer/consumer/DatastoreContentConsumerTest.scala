package uk.co.seanhodges.importer.consumer

import java.util.Date

import com.google.appengine.api.datastore.{DatastoreService, DatastoreServiceFactory, Entity}
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig
import com.google.appengine.tools.development.testing.LocalServiceTestHelper
import org.joda.time.DateTime
import org.scalatest.{BeforeAndAfter, FunSuite}
import uk.co.seanhodges.importer.parser.DatastoreParser
import uk.co.seanhodges.importer.writer.ContentWriter

import scala.collection.mutable.ArrayBuffer

/**
  * Created by sean on 05/01/2017.
  */
class DatastoreContentConsumerTest extends FunSuite with BeforeAndAfter {

  private val helper: LocalServiceTestHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig)
  val datastore: DatastoreService = DatastoreServiceFactory.getDatastoreService

  // Fake writer for inspecting the results
  class TestWriter extends ContentWriter {
    var DB: ArrayBuffer[ArticleMap] = new ArrayBuffer[ArticleMap]()

    override def put(articleData: ArticleMap): Any = DB += articleData
    override def close: Any = ()
  }

  private var consumer: DatastoreContentConsumer = _
  private val writer: TestWriter = new TestWriter

  private def buildArticle(ref: String) = {
    val article: Entity = new Entity("sh_article", ref)
    article.setProperty("heading", "Test article")
    article.setProperty("body", "This is my article")
    article.setProperty("dateCreated", DateTime.now.toDate)
    article.setProperty("dateModified", DateTime.now.toDate)
    article.setProperty("section", "development")
    this.datastore.put(article)
  }

  before {
    helper.setUp()
    for(i <- 1 to 2) this.buildArticle("SH_ART_00000" + i)

    val parser = new DatastoreParser

    this.consumer = new DatastoreContentConsumer(parser, this.writer)
    parser.articleListener = Some(this.consumer) // Listen on parse events
  }

  after {
    helper.tearDown()
  }

  test("consume an XML backup and write to a database") {
    this.consumer.start().end

    assert(this.writer.DB.size === 2)
    assert(this.writer.DB(0)("heading") == "Test article")

    // Check the refs are correct
    for (index <- this.writer.DB.indices) {
      val entry: writer.ArticleMap = this.writer.DB(index)
      val ref: String = entry("ref")
      assert(ref === "SH_ART_00000" + (index + 1))
    }
  }

}
