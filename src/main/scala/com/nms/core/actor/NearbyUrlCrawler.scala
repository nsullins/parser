package com.nms.core.actor

import scala.actors._
import java.net.{URL, HttpURLConnection}
import com.nms.core.parser.GoogleHtmlParser

/**
 * User: nsullins
 * Date: 3/14/11
 * Time: 10:25 PM
 */

object NearbyUrlCrawler extends Actor with GoogleHtmlParser{

  start//start actor

  def extractNearbyUrls(sUrl: String, userId: Int, outerAttr: String, anchorAttr: String){
    Actor.actor{
      val urls = parseGoogleForNearbyURLs(sUrl, outerAttr, anchorAttr){
        x => new URL(x).openStream
      }
      urls.foreach(StoreLocationCrawler ! StoreLocationReq(_, userId, "local-store-desc"))
      //todo refactor hardcoded val to config param use monad reader
    }
  }

  def act() {
    loop{
      react{
        case NearbyUrlReq(url, uid, outerAttr, anchorAttr) => extractNearbyUrls(url, uid, outerAttr, anchorAttr)
        case _ => //log this with monad logger see gmail message
      }
    }
  }
}

case class NearbyUrlReq(sUrl: String, userId: Int, outerAttr: String, anchorAttr: String)

