package uk.co.seanhodges.importer.parser

/**
 * Created by sean on 16/12/16.
 */

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.xml.pull.{EvText, EvElemEnd, EvElemStart, XMLEventReader}

class WPXMLImporterTests extends FunSuite with BeforeAndAfter with ArticleListener {

  type ArticleMap = Map[String, String]

  var articles = ArrayBuffer.empty[ArticleMap]

  override def receiveArticle(articleData: ArticleMap): Unit = {
    articles += articleData
  }

  before {
    articles.clear()
  }

  test("import and read a wordpress XML backup") {

    val xml = new XMLEventReader(Source.fromURL(getClass.getResource("project404.wordpress.2016-12-14.xml"), "UTF-8"))

    val importer = new WPXMLImporter()
    importer.articleListener = Some(this)
    importer.parse(xml)

    assert(articles.length === 14)

    assert(articles(13).getOrElse("title", None) == "Enabling UK Freeview TV in Totem (2.22 or above)")
  }

  // mark that you want a test here in the future
  test("test wordpress XML parsing")(pending)
}
