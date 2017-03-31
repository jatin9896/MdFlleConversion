import java.io.{File, PrintWriter}

import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils


object ReadFile extends App {
  val path2 = "\"https://raw.githubusercontent.com/apache/incubator-carbondata/master/docs/configuration-parameters.md\""
  val path = "https://raw.githubusercontent.com/apache/incubator-carbondata/master/docs/ddl-operation-on-carbondata.md"
  val path3 = "https://raw.githubusercontent.com/apache/incubator-carbondata/master/docs/quick-start-guide.md"
  val result = scala.io.Source.fromURL(path3).mkString
  //println("______>" + result)
  val fileData = getRestContent(result)
  val fileDataAfterConversion = FileModification.ConvertMdExtension(fileData)

  def getRestContent(data: String): String = {
    val httpClient = new DefaultHttpClient()
    val httpRequest: HttpPost = new HttpPost("https://api.github.com/markdown/raw");
    httpRequest.setHeader("Content-type", "text/plain")

    import org.apache.http.entity.StringEntity;
    val test = new StringEntity(data)
    httpRequest.setEntity(test)
    val httpResponse: HttpResponse = httpClient.execute(httpRequest)
    val responseBody = EntityUtils.toString(httpResponse.getEntity())
    // println("--------->" + responseBody.toString)
    /* if(httpResponse.getStatusLine!=200)
       throw new Exception("Rest call fails")
         else
        responseBody.toString()*/
    responseBody.toString()

  }

  def writeToFile(path: String, data: String): Unit = {
    val writer = new PrintWriter(new File("target/test.html"))
    writer.write("<html> <body>" + data + "</body></head>")
    writer.close()
  }

  println("---------------------------------Converted File -------------------------")
  println(fileDataAfterConversion)
  writeToFile("", fileDataAfterConversion)


  //println(getZip())


  //  val repo = Source.fromURL("https://github.com/anmolmehta6669/25th-jan-assignment/archive/master.zip")
  //  println(repo.mkString)
  // val file1:File=new File("https://github.com/anmolmehta6669/25th-jan-assignment/archive/master.zip")
  /*val url:URL=new URL("https://github.com/anmolmehta6669/25th-jan-assignment/archive/master.zip")
  val conn:URLConnection=url.openConnection()
  val in:InputStream=conn.getInputStream
  val out:FileOutputStream=new FileOutputStream("/home/knoldus/Documents/carbon-ui/MdConversionScala/src"+"master.zip")
  var a1:Array[Byte] = new Array[Byte](1024)
  val data=in.read(a1)
  out.write(data)
  out.flush()
  out.close()
  in.close()*/

  //lazy val downloadFromZip = taskKey[Unit]("Download the sbt zip and extract it to ./temp")

  // val out= IO.unzipURL(new URL("https://dl.bintray.com/sbt/native-packages/sbt/0.13.7/sbt-0.13.7.zip"), new File("temp"))
  /*def getZip():String={
      val client:HttpClient = new DefaultHttpClient();
      val request:HttpGet = new HttpGet("https://github.com/anmolmehta6669/25th-jan-assignment/archive/master.zip");
      val response:HttpResponse = client.execute(request);
      val rd  = new InputStreamReader(response.getEntity().getContent());
      val out:FileOutputStream=new FileOutputStream("/home/knoldus/Documents/carbon-ui/MdConversionScala/src"+"file")
      var a1:Array[Byte] = new Array[Byte](1024)
      rd.re
      out.write()
      "Success"

    }*/

}
