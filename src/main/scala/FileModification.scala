import java.io.FileNotFoundException

import org.slf4j.LoggerFactory

import scala.util.matching.Regex

class FileModification {
  val logger = LoggerFactory.getLogger(classOf[FileModification])
  val url = "https://raw.githubusercontent.com/apache/incubator-carbondata/master/docs/"
  val inputFileExtension = ".md"
  val outputFileExtension = ".html"

  val location = "latest/"
  val fileReadObject = new FileRead

  def convertToHtml: String = {
    val listOfFiles = fileReadObject.readListOfFiles()
    val statusList = listOfFiles.map { file =>
      try {
        val fileURLContent = scala.io.Source.fromURL(url + file + inputFileExtension).mkString
        val getFileData = fileReadObject.getFileContent(fileURLContent)
        getFileData match {
          case Some(data: String) => val fileData = ConvertMdExtension(data)
            logger.info("Begin writing [" + file + "." + outputFileExtension + "] at " + location)
            fileReadObject.writeToFile(location + file + outputFileExtension, fileData)
            logger.info("Successfully written [" + file + "." + outputFileExtension + "] at " + location)
            "Success"
          case None => logger.error(s"$file Conversion failed ")
            "Failure"
        }
      }
      catch {
        case exception: FileNotFoundException => logger.error(s"{$file}No Such File Exist on git link $url$file$inputFileExtension")
        case _ => logger.error("Connection Error Please Try Again !!")
      }
    }

    if (statusList.contains("Failure"))
      "Some Files Failed To Convert"
    else
      "All files successfully Converted"
  }

  def ConvertMdExtension(input: String): String = {
    val modifyContentPattern = new Regex("id=\"user-content-")
    val modifyMdPattern = new Regex(".md")
    val contentAfterRemovingUserContent: String = modifyContentPattern replaceAllIn(input, "id=\"")
    val contentAfterReplacingId: String = modifyMdPattern replaceAllIn(contentAfterRemovingUserContent, ".html")
    contentAfterReplacingId
  }


}
