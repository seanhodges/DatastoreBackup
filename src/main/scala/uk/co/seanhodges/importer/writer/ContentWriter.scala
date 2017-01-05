package uk.co.seanhodges.importer.writer

/**
  * Created by sean on 03/01/2017.
  */
trait ContentWriter {

  type ArticleMap = Map[String, String]

  def put(articleData : ArticleMap): Any
  def close: Any
}
