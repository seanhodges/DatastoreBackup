package uk.co.seanhodges.importer.parser

import scala.collection.immutable.HashMap

/**
  * Created by sean on 05/01/2017.
  */
trait ContentParser[A] {

  var articleData = new HashMap[String, String]()
  var articleListener : Option[ArticleListener] = None

  def parse(it: Iterator[A]): Unit

  def sendArticle() {
    this.articleListener.foreach {
      listener => listener.receivesArticle(this.articleData)
    }
  }

}
