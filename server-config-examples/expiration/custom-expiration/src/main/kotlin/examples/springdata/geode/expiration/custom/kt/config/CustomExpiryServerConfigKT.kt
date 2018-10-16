package examples.springdata.geode.expiration.custom.kt.config

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.expiration.custom.expiration.CustomCustomerExpiry
import examples.springdata.geode.expiration.custom.kt.repo.CustomerRepositoryKT
import org.apache.geode.cache.CustomExpiry
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Scope
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@PeerCacheApplication
@EnableLocator
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
class CustomExpiryServerConfigKT {

    @Bean("IDLE")
    internal fun createIdleExpiration() = CustomCustomerExpiry(2)

    @Bean("TTL")
    internal fun createTtlExpiration() = CustomCustomerExpiry(4)

    @Bean("Customers")
    fun createCustomerRegion(gemFireCache: GemFireCache,
                             @Qualifier("IDLE") idleExpiry: CustomExpiry<Long, Customer>,
                             @Qualifier("TTL") ttlExpiry: CustomExpiry<Long, Customer>) =
            ReplicatedRegionFactoryBean<Long, Customer>().apply {
                cache = gemFireCache
                scope = Scope.DISTRIBUTED_ACK
                dataPolicy = DataPolicy.REPLICATE
                setName("Customers")
                isStatisticsEnabled = true
                setCustomEntryIdleTimeout(idleExpiry)
                setCustomEntryTimeToLive(ttlExpiry)
            }
}
