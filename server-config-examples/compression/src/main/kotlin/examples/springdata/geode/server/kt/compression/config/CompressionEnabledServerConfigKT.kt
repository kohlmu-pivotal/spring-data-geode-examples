package examples.springdata.geode.server.kt.compression.config

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.server.compression.repo.CustomerRepo
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.compression.Compressor
import org.apache.geode.compression.SnappyCompressor
import org.springframework.context.annotation.Bean
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@PeerCacheApplication
@EnableLocator
@EnableGemfireRepositories(basePackageClasses = [CustomerRepo::class])
class CompressionEnabledServerConfigKT {

    @Bean
    internal fun createSnappyCompressor() = SnappyCompressor()

    @Bean
    internal fun createCustomerRegion(gemFireCache: GemFireCache, compressor: Compressor) =
            ReplicatedRegionFactoryBean<Long, Customer>().apply {
                cache = gemFireCache
                setRegionName("Customers")
                dataPolicy = DataPolicy.REPLICATE
                setCompressor(compressor)
            }
}
