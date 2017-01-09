package uk.co.seanhodges.importer.parser

import scala.collection.mutable

/**
  * Created by sean on 05/01/2017.
  */
trait ContentParser[A] {

  var articleMarker = "article"
  var articleData: mutable.Map[String, String] = new mutable.HashMap[String, String]()
  var articleListener : Option[ArticleListener] = None

  /**
    * Start digesting article data from the source reader
    * @param it - the concrete read iterator
    */
  def parse(it: Iterator[A]): Unit

  /**
    * Submit the raw article data to the listening consumer
    */
  def sendArticle() {
    this.articleListener.foreach {
      listener => listener.receivesArticle(this.articleData.toMap)
    }
  }

}
