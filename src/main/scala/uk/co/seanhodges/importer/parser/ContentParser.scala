package uk.co.seanhodges.importer.parser

/**
  * Created by sean on 05/01/2017.
  */
trait ContentParser[A] {

  var articleListener : Option[ArticleListener] = None

  def parse(it: Iterator[A]): Unit

}
