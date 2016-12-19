package uk.co.seanhodges.importer.parser

import scala.collection.immutable.HashMap
import scala.xml.pull.{EvElemEnd, EvElemStart, EvText, XMLEventReader}

/**
 * Created by sean on 16/12/16.
 */
class WPXMLImporter {

  var articleListener : Option[ArticleListener] = None

  private var articleData = new HashMap[String, String]()
  private var inArticle = false

  def processData(text: String, currNode: List[String]) {
    if (inArticle) {
      articleData += currNode(0) -> text
    }
  }

  def sendArticle() {
    articleListener.foreach {
      listener => listener.receiveArticle(articleData)
    }
  }

  def parse(xml: XMLEventReader) {

    def loop(currNode: List[String]) {
      if (xml.hasNext) {

        xml.next match {
          case EvElemStart(_, label, _, _) =>
            if (label == "item") {
              articleData.empty
              inArticle = true
            }
            loop(label :: currNode)
          case EvElemEnd(_, label) =>
            if (label == "item") {
              inArticle = false
              sendArticle()
            }
            loop(currNode.tail)
          case EvText(text) =>
            processData(text, currNode)
            loop(currNode)
          case _ => loop(currNode)
        }

      }
    }

    loop(List.empty)
  }

}
