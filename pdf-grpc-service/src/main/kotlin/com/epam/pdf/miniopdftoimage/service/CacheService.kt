package com.epam.pdf.miniopdftoimage.service

import com.epam.pdf.miniopdftoimage.model.FileResponse
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class CacheService(private val redisTemplate: RedisTemplate<String, FileResponse>) {
    fun cacheList(key: String, list: List<FileResponse>) {
        println("PUT Caching list $key")
        val operations = redisTemplate.opsForList()
        operations.rightPushAll(key, *list.toTypedArray())
    }

    fun getCachedList(key: String): List<FileResponse>? {
        println("GET Caching list $key")
        val operations = redisTemplate.opsForList()
        return operations.range(key, 0, -1)
    }

    fun deleteCachedList(key: String) {
        println("DELETE Caching list $key")
        redisTemplate.delete(key)
    }
}