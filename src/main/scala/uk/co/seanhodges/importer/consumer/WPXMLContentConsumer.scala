package uk.co.seanhodges.importer.consumer

import org.slf4j.LoggerFactory
import uk.co.seanhodges.importer.parser.{ArticleListener, ContentParser}
import uk.co.seanhodges.importer.writer.ContentWriter

import scala.collection.mutable
import scala.xml.pull.{XMLEvent, XMLEventReader}

/**
 * Created by sean on 19/12/16.
 */
class WPXMLContentConsumer(parser: ContentParser[XMLEvent], writer: ContentWriter) extends ArticleListener {
  private val logger = LoggerFactory.getLogger(getClass.getName)

  object PARSER_OPTIONS {
    val articleMarker: String = "item"
  }

  def start(xml : XMLEventReader): this.type = {
    logger.debug(s"Consuming WordPress content")
    parser.articleMarker = PARSER_OPTIONS.articleMarker
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
      articleData.getOrElse("title", ""),
      articleData.getOrElse("post_id", "no post_id key"))
    logger.info(s"Processing article ($ref) $heading")

    val articleDataOut: ArticleMap = this.normaliseArticleData(articleData)
    writer.put(articleDataOut)
  }

  def normaliseArticleData(articleData: ArticleMap): ArticleMap = {
    val articleDataOut = new mutable.HashMap[String, String]()
    articleDataOut += (
      "heading" -> articleData.getOrElse("title", ""),
      "body" -> articleData.getOrElse("encoded", "")
    )
    articleDataOut.toMap
  }
}
