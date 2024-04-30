package com.bsipiczki.pdfservice.service

import com.bsipiczki.pdfservice.model.FileResponse
import com.bsipiczki.pdfservice.model.PdfResponse

interface MinioPdfService {
    fun getFiles(bucketName: String): List<FileResponse>
    fun uploadPdf(file: String, contentType: String, name: String): PdfResponse
}