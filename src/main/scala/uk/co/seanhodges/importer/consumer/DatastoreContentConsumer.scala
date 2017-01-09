package uk.co.seanhodges.importer.consumer

import scala.collection.JavaConverters._
import com.google.appengine.api.datastore._
import org.slf4j.LoggerFactory
import uk.co.seanhodges.importer.parser.{ArticleListener, ContentParser}
import uk.co.seanhodges.importer.writer.ContentWriter

/**
  * Created by sean on 09/01/2017.
  */
class DatastoreContentConsumer(parser: ContentParser[Entity], writer: ContentWriter) extends ArticleListener {
  private val logger = LoggerFactory.getLogger(getClass.getName)

  val datastore: DatastoreService = DatastoreServiceFactory.getDatastoreService

  def runQuery: Iterator[Entity] = {
    val q: Query = new Query("sh_article")
    val pq: PreparedQuery = this.datastore.prepare(q)
    pq.asIterator.asScala
  }

  def start(): this.type = {
    logger.debug(s"Getting a list of records")
    val results = runQuery

    logger.debug(s"Consuming datastore content")
    parser.articleListener = Some(this)
    parser.parse(results)
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
