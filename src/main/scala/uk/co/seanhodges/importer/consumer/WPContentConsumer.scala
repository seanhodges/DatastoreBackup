package uk.co.seanhodges.importer.consumer

import org.slf4j.LoggerFactory
import uk.co.seanhodges.importer.parser.{ArticleListener, ContentParser}
import uk.co.seanhodges.importer.writer.ContentWriter

import scala.xml.pull.{XMLEvent, XMLEventReader}

/**
 * Created by sean on 19/12/16.
 */
class WPContentConsumer(parser: ContentParser[XMLEvent], writer: ContentWriter) extends ArticleListener {
  private val logger = LoggerFactory.getLogger(getClass.getName)

  def start(xml : XMLEventReader): this.type = {
    logger.debug(s"Consuming WordPress content")
    parser.articleListener = Some(this)
    parser.parse(xml)
    logger.debug(s"Parser finished")
    this
  }

  def end: this.type = {
    writer.close
    this
  }

  override def receivesArticle(articleData: ArticleMap): Unit = {
    // TODO: Pre-process the article data, then store the article and associated media
    val (heading, ref) = (
      articleData.getOrElse("heading", ""),
      articleData.getOrElse("ref", "no ref key"))
    logger.info(s"Processing article ($ref) $heading")
    writer.put(articleData)
  }

}
