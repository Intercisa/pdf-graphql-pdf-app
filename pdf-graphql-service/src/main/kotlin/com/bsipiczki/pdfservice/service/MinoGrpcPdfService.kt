package com.bsipiczki.pdfservice.service

import com.bsipiczki.pdfservice.model.FileResponse
import com.bsipiczki.pdfservice.model.MetaData
import com.bsipiczki.pdfservice.model.PdfResponse
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Service

@Service
class MinoGrpcPdfService: MinioPdfService {
    private lateinit var pdfServiceGrpc: PdfServiceGrpc.PdfServiceBlockingStub

    init {
        val channel = ManagedChannelBuilder.forAddress("localhost", 6565)
            .usePlaintext()
            .build()
        pdfServiceGrpc = PdfServiceGrpc.newBlockingStub(channel)
    }

    override fun getFiles(bucketName: String): List<FileResponse> {
        val request = Pdf.GetFilesRequest.newBuilder()
            .setBucketName(bucketName)
            .build()
        val filesFromBucket = pdfServiceGrpc.getFilesFromBucket(request)
        return filesFromBucket.filesList.map {
            FileResponse(
                id = it.id,
                name = it.name,
                size = it.size,
                url = it.url
            )
        }
    }

    override fun uploadPdf(file: String, contentType: String, name: String): PdfResponse {
        val request = Pdf.UploadPdfRequest.newBuilder()
            .setFile(file)
            .setContentType(contentType)
            .setName(name)
            .build()
        println("request:  $request")

        val uploadPdf = pdfServiceGrpc.uploadPdf(request)

        return PdfResponse(
            msg = "Pdf uploaded",
            metaData = MetaData(uploadPdf.metaData.title, uploadPdf.metaData.type)
        )
    }
}