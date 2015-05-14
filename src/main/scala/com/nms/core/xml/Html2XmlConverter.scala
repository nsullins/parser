package com.nms.core.xml

import scala.xml._
import java.util.Date
import java.io._
import java.net.URL
import org.w3c.tidy.Tidy

/**
 * User: nsullins
 * Date: 2/15/11
 * Time: 7:39 PM
 */

trait Html2XmlConverter{

  def toXml(input: InputStream): Node = {

    val outputFileName = System.getProperty("java.io.tmpdir") + new Date().getTime.toString
    val tidy = new Tidy()
    tidy.setXmlOut(true)

    try{
      val in = new BufferedInputStream(input)
      val out = new FileOutputStream(outputFileName)
      try{
        tidy.parse(in, out)
      }finally {
        in.close
        out.close
      }
    }catch{
      case e => System.out.println("error converting html to xml", e.printStackTrace)
    }

    XML.loadFile(new File(outputFileName))
  }
}