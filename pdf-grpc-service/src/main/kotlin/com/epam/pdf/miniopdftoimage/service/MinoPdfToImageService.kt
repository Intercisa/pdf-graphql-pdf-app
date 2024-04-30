package com.epam.pdf.miniopdftoimage.service

import PdfServiceGrpc
import com.epam.pdf.miniopdftoimage.constants.MinioBucket.IMAGE_BUCKET
import com.epam.pdf.miniopdftoimage.constants.MinioBucket.PDF_BUCKET
import io.grpc.stub.StreamObserver
import org.lognet.springboot.grpc.GRpcService
import java.io.ByteArrayInputStream
import java.util.*
import kotlin.collections.ArrayList
import com.epam.pdf.miniopdftoimage.model.InputStreamPdf


@GRpcService
class MinoPdfToImageService(
    private val minioService: MinioService,
    private val pdfService: PdfService
) : PdfServiceGrpc.PdfServiceImplBase() {
    override fun uploadPdf(request: Pdf.UploadPdfRequest?, responseObserver: StreamObserver<Pdf.PdfResponse?>) {
        val decodedBytes: ByteArray = Base64.getDecoder().decode(request!!.file)
        val inputStream = ByteArrayInputStream(decodedBytes)
        val streamPdf = InputStreamPdf(inputStream, request.contentType, decodedBytes.size.toLong(), request.name)
        println("request: $request")
        println("streamPdf: $streamPdf")
        minioService.uploadToBucket(file = streamPdf, contentType = streamPdf.contentType)
        pdfService.convertPdfToImage(streamPdf).forEach {
            minioService.uploadToBucket(it, IMAGE_BUCKET)
        }

        val response: Pdf.PdfResponse = Pdf.PdfResponse.newBuilder()
            .setMsg("Pdf uploaded")
            .setMetaData(
                Pdf.MetaData.newBuilder()
                    .setTitle(streamPdf.name)
                    .setType(streamPdf.contentType)
                    .build()
            )
            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    override fun getFilesFromBucket(request: Pdf.GetFilesRequest?, responseObserver: StreamObserver<Pdf.FileResponseList?>) {
        val files: MutableList<Pdf.FileResponse> = ArrayList()
        if (request!!.bucketName == IMAGE_BUCKET) {
            minioService.getFileListResponse(IMAGE_BUCKET).forEach {
                val file = Pdf.FileResponse.newBuilder()
                    .setId(it.id)
                    .setName(it.name)
                    .setSize(it.size)
                    .setUrl(it.url)
                    .build()
                files.add(file)
            }
        } else if (request.bucketName == PDF_BUCKET) {
            minioService.getFileListResponse().forEach {
                val file = Pdf.FileResponse.newBuilder()
                    .setId(it.id)
                    .setName(it.name)
                    .setSize(it.size)
                    .setUrl(it.url)
                    .build()
                files.add(file)
            }
        }

        val response: Pdf.FileResponseList = Pdf.FileResponseList.newBuilder().addAllFiles(files).build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}