package uk.co.seanhodges.importer.writer

import java.io.{File, FileWriter, StringWriter}

import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import scales.utils._
import uk.co.seanhodges.importer.Main.getClass

import scala.io.Codec
import scala.xml.Node

/**
  * Created by sean on 03/01/2017.
  * TODO: This serialization is very inefficient, replace with something like the Scales serializer
  */
class XMLContentWriter extends ContentWriter {
    private val logger = LoggerFactory.getLogger(getClass.getName)

    import scala.xml.Elem

    var articleXml : Elem = <backup></backup>
    private val XML_FILE_DATE_PATTERN = "yyyy-MM-dd-hhmmss"

    private def calculateOutputPath = new File(".").getAbsolutePath + File.separator + "backup-" + DateTime.now().toString(XML_FILE_DATE_PATTERN) + ".xml"

    trait Content {
        def toXml: Elem
    }

    case class Article(var ref: String, var heading: String, var body: String, var section: String, var dateCreated: DateTime, var dateModified: DateTime, var mediaList: Option[Array[ArticleMap]] = None) extends Content {

        def processMediaList(mediaList: Option[Array[ArticleMap]]): Elem =
            { if (mediaList.nonEmpty) <medialist>{mediaList.get.map(media => Content.fromMap(media).toXml)}</medialist> else null }

        def toXml: Elem = {
            <article>
                <ref>{ref}</ref>
                <heading>{heading}</heading>
                <body>{body}</body>
                <section>{section}</section>
                <dateCreated>{dateCreated}</dateCreated>
                <dateModified>{dateModified}</dateModified>
                { this.processMediaList(mediaList) }
            </article>
        }
    }

    case class Media(var id: Long, var url: String) extends Content {

        def toXml: Elem = {
            <media>
                <id>{id}</id>
                <url>{url}</url>
            </media>
        }
    }

    object Content {

        def fromMap(articleMap: ArticleMap): Content = {
            articleMap.toList match {
                // Article match
                case List(("heading", _heading), ("body", _body), ("dateModified", _dateModified), ("ref", _ref), ("section", _section), ("dateCreated", _dateCreated))
                    => Article(_ref, _heading, _body, _section, DateTime.parse(_dateCreated), DateTime.parse(_dateModified))
                case List(("id", _id), ("url", _url))
                    => Media(_id.toLong, _url)
                case _ => throw new Exception("No matching structure for " + articleMap.toList)
            }
        }
    }

    override def put(articleData: ArticleMap): Elem = {
        val entry = Content.fromMap(articleData).toXml
        logger.debug(s"Adding child $entry")
        articleXml = this.addChild(articleXml, entry)
        entry
    }

    private def addChild(n: Node, newChild: Node) = n match {
        case Elem(prefix, label, attribs, scope, child @ _*) =>
            Elem(prefix, label, attribs, scope, child ++ newChild : _*)
        case _ => error("Can only add children to elements!")
    }

    override def toString: String = {
        val writer = new StringWriter()
        scala.xml.XML.write(writer, this.articleXml, Codec.UTF8.name, xmlDecl = true, doctype = null)
        val out = writer.toString
        writer.close()
        out
    }

    override def close: Unit = {
        val outputPath = calculateOutputPath
        logger.info(s"Writing XML output to $outputPath")
        val writer = new FileWriter(outputPath)
        scala.xml.XML.write(writer, this.articleXml, Codec.UTF8.name, xmlDecl = true, doctype = null)
        writer.close()
    }
}