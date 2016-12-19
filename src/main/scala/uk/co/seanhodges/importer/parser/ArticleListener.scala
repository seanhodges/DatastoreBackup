package uk.co.seanhodges.importer.parser

/**
 * Created by sean on 16/12/16.
 */
trait ArticleListener {

  def receiveArticle(articleData : Map[String, String])

}
