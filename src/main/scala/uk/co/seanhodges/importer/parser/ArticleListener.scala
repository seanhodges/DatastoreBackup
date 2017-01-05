package uk.co.seanhodges.importer.parser

/**
 * Created by sean on 16/12/16.
 */
trait ArticleListener {

  type ArticleMap = Map[String, String]

  def receivesArticle(articleData : ArticleMap)

}
