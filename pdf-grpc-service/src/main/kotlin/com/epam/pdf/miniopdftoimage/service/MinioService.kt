package com.epam.pdf.miniopdftoimage.service


import com.epam.pdf.miniopdftoimage.constants.ImageType.PNG
import com.epam.pdf.miniopdftoimage.constants.MinioBucket.PDF_BUCKET
import com.epam.pdf.miniopdftoimage.model.FileResponse
import com.epam.pdf.miniopdftoimage.model.InputStreamPdf
import io.minio.BucketExistsArgs
import io.minio.GetPresignedObjectUrlArgs
import io.minio.ListObjectsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.ObjectWriteResponse
import io.minio.PutObjectArgs
import io.minio.Result
import io.minio.http.Method
import io.minio.messages.Item
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.TimeUnit


private object MinioFactory {
    private const val URL = "http://192.168.1.163:9000"
    private const val USER = "MINIO"
    private const val PASSWORD = "SECRET_SECRET"

    private val minioClient: MinioClient by lazy {
        val client = MinioClient.builder()
            .endpoint(URL)
            .credentials(USER, PASSWORD)
            .build()

        if (!client.bucketExists(BucketExistsArgs.builder().bucket(PDF_BUCKET).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(PDF_BUCKET).build())
        }

        client
    }

    fun getInstance() = minioClient
}

@Service
class MinioService(
    private val cacheService: CacheService
) {
    fun uploadToBucket(file: InputStreamPdf, bucket: String = PDF_BUCKET, contentType: String = PNG): ObjectWriteResponse {
        val cachedList = cacheService.getCachedList(bucket)
        if (!cachedList.isNullOrEmpty()) {
            cacheService.deleteCachedList(bucket)
        }
        return MinioFactory.getInstance().putObject(
            PutObjectArgs.builder()
                .bucket(bucket)
                .`object`(file.name)
                .contentType(contentType)
                .stream(file.inputStream, file.size, -1)
                .build()
        )
    }


   private fun getUrl(bucket: String = PDF_BUCKET, fileName: String): String =
        MinioFactory.getInstance().getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucket)
                .`object`(fileName)
                .expiry(2, TimeUnit.HOURS)
                .build()
        )

    private fun listObjects(bucket: String = PDF_BUCKET): MutableIterable<Result<Item>> =
        MinioFactory.getInstance()
        .listObjects(
            ListObjectsArgs.builder()
                .bucket(bucket)
                .build()
        )

    fun getFileListResponse(bucket: String = PDF_BUCKET): List<FileResponse> {
        val cachedList = cacheService.getCachedList(bucket)
        return if (cachedList.isNullOrEmpty()) {
            val responses = listObjects(bucket).map {
                val file = it.get()
                FileResponse(
                    id = UUID.randomUUID().toString(),
                    name = file.objectName(),
                    size = file.size(),
                    url = getUrl(bucket, file.objectName())
                )
            }
            cacheService.cacheList(bucket, responses)
            responses
        } else {
            cachedList
        }
    }
}