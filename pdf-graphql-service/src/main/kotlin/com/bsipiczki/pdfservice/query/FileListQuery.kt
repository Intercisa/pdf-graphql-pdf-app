package com.bsipiczki.pdfservice.query

import com.bsipiczki.pdfservice.service.MinioPdfService
import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component

@Component
class FileListQuery(
    private val minioPdfService: MinioPdfService
): Query {
    fun listFilesGrpc(
        bucketName: String
    ) = minioPdfService.getFiles(bucketName)
}