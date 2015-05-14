package com.nms.core.parser

import com.geo.rem.test.support.MyTestSuite
import org.scalatest.matchers.ShouldMatchers
import scala.xml._
import java.io.FileInputStream



/**
 * User: nsullins
 * Date: 2/13/11
 * Time: 12:16 PM
 */

class HtmlParserTest extends MyTestSuite with ShouldMatchers{

  //need to figure out setUp for this test

  //TODO refactor the html source and place under resources test folder

  test("find top 10 nearby urls"){

    val source = "./searchResults.html"
    val anchorAttr = "tiny-pin"
    val outerNode = "ires"
    //must use curlies because we are creating an anonymous class not instantiating a trait
    val htmlParser = new GoogleHtmlParser{}
    val list = htmlParser.parseGoogleForNearbyURLs(source, outerNode, anchorAttr){x => new FileInputStream(x)}
    list.length should be (10)
  }

  test("find all addresses"){

    val source = "./locations.html"
    val searchAttr = "local-store-desc"
    //must use curlies because we are creating an anonymous class not instantiating a trait
    val htmlParser = new GoogleHtmlParser{}
    val list = htmlParser.parseGoolgeForLocations(source, searchAttr){x => new FileInputStream(x)}
    list.length should be (8)
  }

  test("extract phone from string"){

    val parser = new GoogleHtmlParser{}
    var res = parser.extractPhone("gobbledeegook")
    res should be (None)

    res = parser.extractPhone("some more gobbledeegook plus a number (415) 503 7843 plus some gook")
    res should be(Some("(415) 503 7843"))
  }

}