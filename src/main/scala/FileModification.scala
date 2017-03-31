import scala.util.matching.Regex

object FileModification {
  def ConvertMdExtension(input:String): String ={
    val modifyContentPattern= new Regex("id=\"user-content-")
    val modifyMdPattern=new Regex(".md")
    val newinput:String= modifyContentPattern replaceAllIn (input,"id=\"")
    val newinput1:String= modifyMdPattern replaceAllIn (newinput,".html")
    newinput1
  }

  val fileData=ConvertMdExtension("<a id=\"user-content-spark-configuration.md\" class=\"anchor\" href=\"#spark-configuration\" aria-hidden=\"true\">")
 //  println(fileData)
}
