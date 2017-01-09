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
class WPXMLContentConsumerTest extends FunSuite with BeforeAndAfter {

  // Fake writer for inspecting the results
  class TestWriter extends ContentWriter {
    var DB: ArrayBuffer[ArticleMap] = new ArrayBuffer[ArticleMap]()

    override def put(articleData: ArticleMap): Any = DB += articleData
    override def close: Any = ()
  }

  private var consumer: WPXMLContentConsumer = _
  private val writer: TestWriter = new TestWriter

  before {
    // Fake the XML parser so we can simulate the parsing process
    class XMLParser extends ContentParser[XMLEvent] {
      private def buildArticle(ref: String): mutable.HashMap[String, String] = {
        var articleData = new mutable.HashMap[String, String]()
        articleData += ("post_id" -> ref,
          "title" -> "Extending Nautilus",
          "encoded"    -> "This is a test article",
          "section" -> "Uncategorized")
        articleData
      }

      override def parse(xml: Iterator[XMLEvent]): Unit = {
        for(i <- 0 to 1) {
          this.articleData = this.buildArticle(i.toString)
          this.sendArticle() // Push each article to the consumer
        }
        xml.next()
      }
    }

    val parser = new XMLParser

    this.consumer = new WPXMLContentConsumer(parser, this.writer)
    parser.articleListener = Some(this.consumer) // Listen on parse events
  }

  test("consume a wordpress XML backup and write to a database") {
    // Cut down representation of the WP backup format
    val xml =
      <backup>
        <item>
          <title>Extending Nautilus</title>
          <pubDate>Fri, 09 Mar 2007 12:56:12 +0000</pubDate>
          <dc:creator>seanhodges</dc:creator>
          <content:encoded><![CDATA[This is a test article]]></content:encoded>
          <excerpt:encoded><![CDATA[]]></excerpt:encoded>
          <wp:post_id>14</wp:post_id>
          <wp:post_date>2007-03-09 12:56:12</wp:post_date>
          <wp:post_date_gmt>2007-03-09 12:56:12</wp:post_date_gmt>
          <category domain="category" nicename="uncategorized"><![CDATA[Uncategorized]]></category>
        </item>
        <item>
          <title>Roll your own Debian/Ubuntu package</title>
          <pubDate>Mon, 30 Nov -0001 00:00:00 +0000</pubDate>
          <dc:creator>seanhodges</dc:creator>
          <content:encoded><![CDATA[This is another article]]></content:encoded>
          <excerpt:encoded><![CDATA[]]></excerpt:encoded>
          <wp:post_id>16</wp:post_id>
          <wp:post_date>0000-00-00 00:00:00</wp:post_date>
          <wp:post_date_gmt>0000-00-00 00:00:00</wp:post_date_gmt>
        </item>
      </backup>

    this.consumer.start(new XMLEventReader(Source.fromString(xml.toString))).end

    assert(this.writer.DB.size === 2)
    println(this.writer.DB(0))
    assert(this.writer.DB(0)("heading") == "Extending Nautilus")
    assert(this.writer.DB(0)("body") == "This is a test article")
  }

}
