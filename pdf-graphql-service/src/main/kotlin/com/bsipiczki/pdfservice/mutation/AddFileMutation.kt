package com.bsipiczki.pdfservice.mutation

import com.bsipiczki.pdfservice.model.PdfResponse
import com.bsipiczki.pdfservice.model.UploadPdfRequest
import com.bsipiczki.pdfservice.service.MinioPdfService
import com.expediagroup.graphql.spring.operations.Mutation
import org.springframework.stereotype.Component


@Component
class AddFileMutation(
    private val minioPdfService: MinioPdfService
) : Mutation {
    fun uploadPdfGrpc(uploadPdfRequest: UploadPdfRequest): PdfResponse {
        println("uploadPdfRequest:  $uploadPdfRequest")
        return minioPdfService.uploadPdf(
            file = uploadPdfRequest.file,
            contentType = uploadPdfRequest.contentType,
            name = uploadPdfRequest.name
        )
    }
}
