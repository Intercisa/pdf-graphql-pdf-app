package com.bsipiczki.pdfservice.model

data class PdfResponse(
    val msg : String,
    val metaData: MetaData
)

data class MetaData(
    val title : String,
    val type : String
)

data class FileResponse(
    val id: String,
    val name: String,
    val size: Long,
    val url: String
)

data class UploadPdfRequest(
    val file: String,
    val contentType: String,
    val name: String
)
