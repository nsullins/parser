package com.nms.core.actor

import scala.actors._
import java.net.URL
import com.nms.core.parser.GoogleHtmlParser

/**
 * User: nsullins
 * Date: 3/27/11
 * Time: 1:17 PM
 */

object StoreLocationCrawler extends Actor with GoogleHtmlParser{

  start//start actor

  //todo modify this at some point to include the item that was searched on so that we can add it to the new store object
  //as a product that it sells
  def extractStoreLocations(sUrl: String, userId: Int, searchAttr: String){
    Actor.actor{
      val locations = parseGoolgeForLocations(sUrl, searchAttr){
        x => new URL(x).openStream
      }
      //todo this is where we store the data in mongo, but first get a test case with out mongo and then with mongo
      //locations foreach
    }
  }

  def act() {
    loop{
      react{
        case StoreLocationReq(url, uid, attr) => extractStoreLocations(url, uid, attr)
        case _ => //log this with monad logger see gmail message
      }
    }
  }
}

case class StoreLocationReq(sUrl: String, userId: Int, searchAttr: String)