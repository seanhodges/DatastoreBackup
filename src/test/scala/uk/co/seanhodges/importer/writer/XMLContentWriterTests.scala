package uk.co.seanhodges.importer.writer

/**
 * Created by sean on 16/12/16.
 */

import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.immutable.HashMap
import scala.xml.Elem

class XMLContentWriterTests extends FunSuite with BeforeAndAfter {

  def cleanXml(in: Elem): String = in.toString.replaceAll(" ", "").replaceAll("\n", "")

  before {
  }

  test("writes an article to XML") {

    // Prepares the new entity
    var articleData = new HashMap[String, String]()
    articleData += (
                    "ref"           -> "ref123",
                    "heading"       -> "My test article",
                    "body"          -> "This is a test article",
                    "section"       -> "test",
                    "dateCreated"   -> "2016-09-09T13:00",
                    "dateModified"  -> "2016-09-09T13:00")

    val writer = new XMLContentWriter()
    val result : Elem = writer.put(articleData)

    val expected: Elem =
                  <article>
                      <ref>ref123</ref>
                      <heading>My test article</heading>
                      <body>This is a test article</body>
                      <section>test</section>
                      <dateCreated>2016-09-09T13:00:00.000+01:00</dateCreated>
                      <dateModified>2016-09-09T13:00:00.000+01:00</dateModified>
                  </article>
    assert(this.cleanXml(result) == this.cleanXml(expected))
  }

  test("writes multiple articles to XML") {

    // Prepares the new entity
    val writer = new XMLContentWriter()

    var articleData = new HashMap[String, String]()
    articleData += (
      "ref"           -> "ref123",
      "heading"       -> "My test article",
      "body"          -> "This is a test article",
      "section"       -> "test",
      "dateCreated"   -> "2016-09-09T13:00",
      "dateModified"  -> "2016-09-09T13:00")
    writer.put(articleData)

    articleData = articleData.empty
    articleData += (
      "ref"           -> "ref456",
      "heading"       -> "My second test article",
      "body"          -> "This is a second test article",
      "section"       -> "test",
      "dateCreated"   -> "2016-09-09T14:00",
      "dateModified"  -> "2016-09-09T14:00")
    writer.put(articleData)

    val result : Elem = writer.articleXml

    val expected: Elem =
              <backup>
                <article>
                  <ref>ref123</ref>
                  <heading>My test article</heading>
                  <body>This is a test article</body>
                  <section>test</section>
                  <dateCreated>2016-09-09T13:00:00.000+01:00</dateCreated>
                  <dateModified>2016-09-09T13:00:00.000+01:00</dateModified>
                </article>
                <article>
                  <ref>ref456</ref>
                  <heading>My second test article</heading>
                  <body>This is a second test article</body>
                  <section>test</section>
                  <dateCreated>2016-09-09T14:00:00.000+01:00</dateCreated>
                  <dateModified>2016-09-09T14:00:00.000+01:00</dateModified>
                </article>
              </backup>
    assert(this.cleanXml(result) == this.cleanXml(expected))
  }
}
