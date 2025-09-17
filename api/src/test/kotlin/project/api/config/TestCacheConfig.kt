import org.springframework.boot.test.context.TestConfiguration
import org.springframework.cache.CacheManager
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestCacheConfig {

    @Bean
    fun cacheManager(): CacheManager {
        return ConcurrentMapCacheManager() // Использует in-memory кеш вместо Redis
    }
}