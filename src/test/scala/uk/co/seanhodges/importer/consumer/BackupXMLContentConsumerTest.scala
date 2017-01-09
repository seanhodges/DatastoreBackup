package uk.co.seanhodges.importer.consumer

import org.scalatest.{BeforeAndAfter, FunSuite}
import uk.co.seanhodges.importer.parser.ContentParser
import uk.co.seanhodges.importer.writer.ContentWriter

import scala.collection.immutable.HashMap
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.xml.pull.{XMLEvent, XMLEventReader}

/**
  * Created by sean on 05/01/2017.
  */
class BackupXMLContentConsumerTest extends FunSuite with BeforeAndAfter {

  // Fake writer for inspecting the results
  class TestWriter extends ContentWriter {
    var DB: ArrayBuffer[ArticleMap] = new ArrayBuffer[ArticleMap]()

    override def put(articleData: ArticleMap): Any = DB += articleData
    override def close: Any = ()
  }

  private var consumer: BackupXMLContentConsumer = _
  private val writer: TestWriter = new TestWriter

  before {
    // Fake the XML parser so we can simulate the parsing process
    class XMLParser extends ContentParser[XMLEvent] {
      private def buildArticle(ref: String): mutable.Map[String, String] = {
        var articleData: mutable.Map[String, String] = new mutable.HashMap[String, String]()
        articleData += ("ref" -> ref,
          "heading" -> "My test article",
          "body"    -> "This is a test article",
          "section" -> "test")
        articleData
      }

      override def parse(xml: Iterator[XMLEvent]): Unit = {
        for(i <- 0 to 1) {
          this.articleData = this.buildArticle("art" + i)
          this.sendArticle() // Push each article to the consumer
        }
        xml.next()
      }
    }

    val parser = new XMLParser

    this.consumer = new BackupXMLContentConsumer(parser, this.writer)
    parser.articleListener = Some(this.consumer) // Listen on parse events
  }

  test("consume an XML backup and write to a database") {
    val xml =
      <backup>
        <article>
          <ref>art0</ref>
          <heading>My test article</heading>
          <body>This is a test article</body>
          <section>test</section>
          <dateCreated>2016-09-09T13:00:00.000+01:00</dateCreated>
          <dateModified>2016-09-09T13:00:00.000+01:00</dateModified>
        </article>
        <article>
          <ref>art1</ref>
          <heading>My second test article</heading>
          <body>This is a second test article</body>
          <section>test</section>
          <dateCreated>2016-09-09T14:00:00.000+01:00</dateCreated>
          <dateModified>2016-09-09T14:00:00.000+01:00</dateModified>
        </article>
      </backup>

    this.consumer.start(new XMLEventReader(Source.fromString(xml.toString))).end

    assert(this.writer.DB.size === 2)
    assert(this.writer.DB(0)("heading") == "My test article")

    // Check the refs are correct
    for (index <- this.writer.DB.indices) {
      val entry: writer.ArticleMap = this.writer.DB(index)
      val ref: String = entry("ref")
      assert(ref === "art" + index)
    }
  }

}
