package uk.co.seanhodges.importer.consumer

import uk.co.seanhodges.importer.parser.{ArticleListener, ContentParser}
import uk.co.seanhodges.importer.writer.ContentWriter

import scala.xml.pull.{XMLEvent, XMLEventReader}

/**
 * Created by sean on 19/12/16.
 */
class WPContentConsumer(parser: ContentParser[XMLEvent], writer: ContentWriter) extends ArticleListener {

  def start(xml : XMLEventReader): this.type = {
    parser.articleListener = Some(this)
    parser.parse(xml)
    this
  }

  def end: this.type = {
    writer.close
    this
  }

  override def receivesArticle(articleData: ArticleMap): Unit = {
    // TODO: Pre-process the article data, then store the article and associated media
    writer.put(articleData)
  }

}
