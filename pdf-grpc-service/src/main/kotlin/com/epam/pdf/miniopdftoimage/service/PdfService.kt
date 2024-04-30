package com.epam.pdf.miniopdftoimage.service

import com.epam.pdf.miniopdftoimage.constants.ImageExtension.PNG_EXT
import com.epam.pdf.miniopdftoimage.model.InputStreamPdf
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Service
class PdfService {
    fun convertPdfToImage(file: InputStreamPdf): MutableList<InputStreamPdf> {
        file.inputStream.reset()
        val document: PDDocument = PDDocument.load(file.inputStream)
        val pdfRenderer = PDFRenderer(document)
        val pdfInputStreamList = mutableListOf<InputStreamPdf>()
        document.pages.withIndex().forEach {
            val bim = pdfRenderer.renderImageWithDPI(it.index, 300f, ImageType.RGB)
            val os = ByteArrayOutputStream()
            ImageIO.write(bim, PNG_EXT, os)
            val inputStream = ByteArrayInputStream(os.toByteArray())
            val formattedName = file.name.substringBeforeLast(".")
            pdfInputStreamList.add(
                InputStreamPdf(
                    inputStream = inputStream,
                    contentType = file.contentType,
                    size = os.size().toLong(),
                    name = "${formattedName}_${it.index}.$PNG_EXT"
                )
            )
        }
        document.close()
        return pdfInputStreamList
    }
}