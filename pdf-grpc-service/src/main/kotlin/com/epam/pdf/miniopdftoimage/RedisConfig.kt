package com.epam.pdf.miniopdftoimage

import com.epam.pdf.miniopdftoimage.model.FileResponse
import org.springframework.cache.annotation.CachingConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import java.time.Duration

@Configuration
class RedisConfig: CachingConfigurer {
    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, FileResponse> {
        val template = RedisTemplate<String, FileResponse>()
        template.connectionFactory = connectionFactory
        template.keySerializer = GenericJackson2JsonRedisSerializer()
        template.valueSerializer = GenericJackson2JsonRedisSerializer()
        return template
    }

    @Bean
    fun cacheManager(connectionFactory: RedisConnectionFactory): RedisCacheManager {
        val redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(redisCacheConfiguration)
            .build()
    }
}