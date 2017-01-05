package uk.co.seanhodges.importer.parser

import scala.collection.immutable.HashMap
import scala.xml.pull._

/**
  * Created by sean on 16/12/16.
  */
class WPXMLParser extends ContentParser[XMLEvent] {

  private var articleData = new HashMap[String, String]()
  private var inArticle = false

  private def processData(text: String, currNode: List[String]) {
    if (inArticle) {
      articleData += currNode.head -> text
    }
  }

  private def sendArticle() {
    articleListener.foreach {
      listener => listener.receivesArticle(articleData)
    }
  }

  def parse(xml: Iterator[XMLEvent]): Unit = {

    def loop(currNode: List[String]) {
      if (xml.hasNext) {

        xml.next match {
          case EvElemStart(_, label, _, _) =>
            if (label == "item") {
              articleData = articleData.empty
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
