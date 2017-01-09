package uk.co.seanhodges.importer.parser

/**
 * Created by sean on 16/12/16.
 */

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.xml.pull.{EvText, EvElemEnd, EvElemStart, XMLEventReader}

class XMLParserTests extends FunSuite with BeforeAndAfter with ArticleListener {

  var articles = ArrayBuffer.empty[ArticleMap]

  override def receivesArticle(articleData: ArticleMap): Unit = {
    articles += articleData
  }

  before {
    articles.clear()
  }

  test("import and read a wordpress XML backup") {

    val xml = new XMLEventReader(Source.fromURL(getClass.getResource("project404.wordpress.2016-12-14.xml"), "UTF-8"))

    val importer = new XMLParser()

    // Set up for WP XML parsing
    importer.articleMarker = "item"

    importer.articleListener = Some(this)
    importer.parse(xml)

    assert(articles.length === 14)

    assert(articles(13).getOrElse("title", None) == "Enabling UK Freeview TV in Totem (2.22 or above)")
  }


  test("import and read a datastore backup XML file") {

    val xml = new XMLEventReader(Source.fromURL(getClass.getResource("datastore-backup-2017-01-08-200415.xml"), "UTF-8"))

    val importer = new XMLParser()
    importer.articleListener = Some(this)
    importer.parse(xml)

    assert(articles.length === 2)

    assert(articles(0).getOrElse("heading", None) == "My test article")
  }
}
