import sbt._

class CoreProject(info: ProjectInfo) extends DefaultProject(info){

  override def libraryDependencies = Set(
      "org.scalatest" % "scalatest" % "1.2" % "compile->default",
      "org.scala-tools.testing" % "specs_2.8.1" % "1.6.7.2" % "compile->default",
      "org.scala-tools.testing" % "scalacheck_2.8.1" % "1.8" % "compile->default",
      "net.sf.jtidy" % "jtidy" % "r938" % "compile->default"
    ) ++ super.libraryDependencies

}
