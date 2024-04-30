package com.epam.pdf.miniopdftoimage.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.ByteArrayInputStream

data class PdfResponse(
    val msg : String,
    val metaData: MetaData
)

data class MetaData(
    val title : String,
    val type : String
)

data class FileResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("size") val size: Long,
    @JsonProperty("url") val url: String
)

data class InputStreamPdf(
    val inputStream: ByteArrayInputStream,
    val contentType: String,
    val size: Long,
    val name: String
)

data class UploadPdfRequest(
    val file: String,
    val contentType: String,
    val name: String
)
