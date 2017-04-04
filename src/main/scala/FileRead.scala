import java.io.{File, PrintWriter}

import com.typesafe.config.ConfigFactory
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.conn.ConnectTimeoutException
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.slf4j.LoggerFactory

import scala.io.Source

class FileRead {
  val logger = LoggerFactory.getLogger(classOf[FileModification])
  val headerContents: String = Source.fromFile("src/htmls/header.html").mkString

  def getFileContent(data: String): Option[String] = {
    val httpClient = new DefaultHttpClient()
    val httpRequest: HttpPost = new HttpPost("https://api.github.com/markdown/raw");

    httpRequest.setHeader("Content-type", "text/plain")

    import org.apache.http.entity.StringEntity;
    val test = new StringEntity(data)
    httpRequest.setEntity(test)
    try{
      val httpResponse: HttpResponse = httpClient.execute(httpRequest)
      val responseBody = EntityUtils.toString(httpResponse.getEntity())
      logger.info(s"status : {${httpResponse.getStatusLine.toString.contains("OK")}}")
      if (httpResponse.getStatusLine.toString.contains("OK"))
        Some(responseBody.toString)
      else {
        logger.error(s"Fetching file from {$httpRequest} fails {${httpResponse.getStatusLine}}")
        None
      }
    }
    catch  {
      case exception:ConnectTimeoutException=>
      logger.error("Timeout Exception ")
        None
    }

  }

  def readListOfFiles(): List[String] = {
    import scala.collection.JavaConverters._
    val listOfFiles = ConfigFactory.load().getStringList("fileList").asScala.toList
    logger.info(s"List of files : $listOfFiles")
    listOfFiles
  }

  def writeToFile(path: String, data: String): Int = {
    val writer = new PrintWriter(new File(path))
    writer.write(headerContents + data + "</body></head>")
    writer.close()
    data.length
  }
}
