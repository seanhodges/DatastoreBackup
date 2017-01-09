package uk.co.seanhodges.importer.parser


/**
 * Created by sean on 16/12/16.
 */
trait ArticleListener {

  type ArticleMap = Map[String, String]

  /**
    * When an article read is complete a listener receives the event here
    * @param articleData - the raw parsed article data
    */
  def receivesArticle(articleData : ArticleMap)

}
