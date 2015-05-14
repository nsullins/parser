package com.nms.core.parser

import scala.xml._
import scala.io.{Source => ioSource}
import scala.io.BufferedSource
import com.nms.core.xml.Html2XmlConverter
import java.io.InputStream


/**
 * User: nsullins
 * Date: 2/21/11
 * Time: 2:32 PM
 */

/*
This class used for parsing google search results
 */
trait GoogleHtmlParser extends Html2XmlConverter{

  /**
   * Curried funtion for parsing html for location data. The html associated with the URL will contain store location
   * data. The toXml inherited method converts the html to xml
   *
   * @param url specifies the source to be parsed for location data
   * @param searchAttr the html element attribute to key on when searching for location data
   * @param function that takes a string and returns an inputstream. in test this is fileinputstream and http stream live
   *
   * @return list of strings for each location found in source
   */
  protected[parser] def parseGoolgeForLocations(url: String, searchAttr: String)(f: String => InputStream): List[String] = {
    parseLocations(toXml(f(url)), searchAttr)
  }

  /**
   * This method parses the xml node using the searchAttr to key on the correct xml element
   *
   * @param xml The xml containing the location elements we're interested in
   * @param searchAttr The element to key on when searching for location data
   *
   * @return The list of strings containing the store locations
   */
  protected[parser] def parseLocations(xml: Node, searchAttr: String): List[String] = {

    val locations = (xml \\ "_" filter attributeValueEquals(searchAttr))
    locations.map(_.text)//get string values for each element
      .filter(extractPhone(_) != None)//use location data only when phone is present...more of a error guard than BI
      .map(f => f.substring(0, f.indexOf(extractPhone(f).get))).toList//split at phone and return result as list
  }

  /**
   * Extract phone number from string
   */
  protected[parser] def extractPhone(str: String): Option[String] = {
    (("""\(?(\d{3})\)?[- ]?(\d{3})[- ]?(\d{4})""".r) findFirstIn str)
  }

  /**
   * Curried function just like parseGoolgeForLocations above but returns nearby urls that are then to be crawled for locations
   *
   */
  protected[parser] def parseGoogleForNearbyURLs(url: String, outerAttr: String, anchorAttr: String)(f: String => InputStream)
    : List[String] = {
    parseNearbyURLs(toXml(f(url)), outerAttr, anchorAttr)
  }

  /**
   * Parses the xml passed for the actual nearby urls
   */
  protected[parser] def parseNearbyURLs(xml: Node, nodeAttr: String, anchorAttr: String): List[String] = {
    val liNodes = (xml \\ "_" filter attributeValueEquals(nodeAttr)) \\ "li"
    val listToBe =
      for{node <- liNodes
        anchor = node \\ "_" filter attributeValueEquals(anchorAttr)
        if(!anchor.isEmpty)
        result = (anchor \ "@href").text
    }yield result
    listToBe.toList
  }

  protected[parser] def loadSource(sourceUrl: String): BufferedSource = {
    ioSource.fromURL(sourceUrl)
  }

  private def attributeValueEquals(value: String)(node: Node): Boolean = {
    node.attributes.exists(_.value.toString == value)
  }

}