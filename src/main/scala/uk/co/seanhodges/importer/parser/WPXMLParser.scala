package uk.co.seanhodges.importer.parser

import org.slf4j.LoggerFactory

import scala.collection.immutable.HashMap
import scala.xml.pull._

/**
  * Created by sean on 16/12/16.
  */
class WPXMLParser extends ContentParser[XMLEvent] {
  private val logger = LoggerFactory.getLogger(getClass.getName)

  private var inArticle = false

  private def processData(text: String, currNode: List[String]) {
    if (inArticle) {
      logger.debug(s"Processing $text for $currNode.head")
      this.articleData += currNode.head -> text
    }
  }

  def parse(xml: Iterator[XMLEvent]): Unit = {

    def loop(currNode: List[String]) {
      if (xml.hasNext) {

        xml.next match {
          case EvElemStart(_, label, _, _) =>
            if (label == "item") {
              this.articleData = this.articleData.empty
              this.inArticle = true
              logger.debug(s"Found new article, clearing state")
            }
            loop(label :: currNode)
          case EvElemEnd(_, label) =>
            if (label == "item") {
              this.inArticle = false
              logger.debug(s"Article end - sending to consumer")
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
