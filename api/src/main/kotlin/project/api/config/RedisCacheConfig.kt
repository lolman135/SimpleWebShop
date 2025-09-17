package project.api.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import project.api.dto.response.business.*
import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID

@Configuration
class RedisCacheConfig {

    @Bean
    fun cacheManager(redisConnectionFactory: RedisConnectionFactory): CacheManager {
        val ptv = BasicPolymorphicTypeValidator.builder()
            .allowIfSubType("project.api.dto.response.business")
            .allowIfSubType("project.api.dto.response.business.subDto")
            .allowIfSubType(List::class.java)
            .allowIfSubType(Map::class.java)
            .allowIfSubType(LocalDateTime::class.java)
            .allowIfSubType(UUID::class.java)
            .allowIfSubType(String::class.java)
            .build()

        val baseObjectMapper: ObjectMapper = jacksonObjectMapper().apply {
            registerKotlinModule()
            registerModule(JavaTimeModule())
            activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
            disable(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS)
        }

        val defaultSerializer = GenericJackson2JsonRedisSerializer(baseObjectMapper)
        val defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(defaultSerializer))
            .entryTtl(Duration.ofMinutes(40))
            .disableCachingNullValues()

        val userSerializer = Jackson2JsonRedisSerializer(baseObjectMapper, UserDtoResponse::class.java)
        val categorySerializer = Jackson2JsonRedisSerializer(baseObjectMapper, CategoryDtoResponse::class.java)
        val productSerializer = Jackson2JsonRedisSerializer(baseObjectMapper, ProductDtoResponse::class.java)
        val roleSerializer = Jackson2JsonRedisSerializer(baseObjectMapper, RoleDtoResponse::class.java)
        val feedbackSerializer = Jackson2JsonRedisSerializer(baseObjectMapper, FeedbackDtoResponse::class.java)
        val orderSerializer = Jackson2JsonRedisSerializer(baseObjectMapper, OrderDtoResponse::class.java)

        val userConfig = defaultConfig.serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(userSerializer)
        )
        val categoryConfig = defaultConfig.serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(categorySerializer)
        )
        val productConfig = defaultConfig.serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(productSerializer)
        )
        val roleConfig = defaultConfig.serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(roleSerializer)
        )
        val feedbackConfig = defaultConfig.serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(feedbackSerializer)
        )
        val orderConfig = defaultConfig.serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(orderSerializer)
        )

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(defaultConfig)
            .withCacheConfiguration("users", userConfig)
            .withCacheConfiguration("categories", categoryConfig)
            .withCacheConfiguration("products", productConfig)
            .withCacheConfiguration("roles", roleConfig)
            .withCacheConfiguration("feedbacks", feedbackConfig)
            .withCacheConfiguration("orders", orderConfig)
            .build()
    }
}