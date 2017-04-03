import java.io.{File, PrintWriter}

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

class FileModification {
  val logger = LoggerFactory.getLogger(classOf[FileModification])
  val url = "https://raw.githubusercontent.com/apache/incubator-carbondata/master/docs/"
  val inputFileExtension = ".md"
  val outputFileExtension = ".html"

  import scala.io.Source

  val headerContents: String = Source.fromFile("src/htmls/header.html").mkString

  val location = "latest/"
  val fileReadObject = new FileRead

  def convertToHtml: String = {
    val listOfFiles = readListOfFiles()
    val statusList = listOfFiles.map { file =>
      val fileURLContent = scala.io.Source.fromURL(url + file + inputFileExtension).mkString
      val getFileData = fileReadObject.getFileContent(fileURLContent)
      getFileData match {
        case Some(data: String) => val fileData = fileReadObject.ConvertMdExtension(data)
          logger.info("Begin writing [" + file + "." + outputFileExtension + "] at " + location)
          writeToFile(location + file + outputFileExtension, fileData)
          logger.info("Successfully written [" + file + "." + outputFileExtension + "] at " + location)
          "Success"
        case None => logger.error(s"$file Conversion failed ")
          "Failure"
      }
    }

    if (statusList.contains("Failure"))
      "Some Files Failed To Convert"
    else
      "All files successfully Converted"
  }

  private def readListOfFiles(): List[String] = {
    import scala.collection.JavaConverters._
    val listOfFiles = ConfigFactory.load().getStringList("fileList").asScala.toList
    logger.info(s"List of files : $listOfFiles")
    listOfFiles
  }

  private def writeToFile(path: String, data: String): Unit = {
    val writer = new PrintWriter(new File(path))
    writer.write(headerContents + data + "</body></head>")
    writer.close()
  }
}
