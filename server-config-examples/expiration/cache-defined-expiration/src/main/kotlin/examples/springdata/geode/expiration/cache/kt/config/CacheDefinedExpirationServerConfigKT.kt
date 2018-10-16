package examples.springdata.geode.expiration.cache.kt.config

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.expiration.cache.kt.repo.CustomerRepositoryKT
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Scope
import org.springframework.context.annotation.Bean
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.EnableExpiration
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import org.springframework.data.gemfire.expiration.ExpirationActionType
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@PeerCacheApplication
@EnableLocator
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@EnableExpiration(policies = [
    EnableExpiration.ExpirationPolicy(timeout = 4, action = ExpirationActionType.DESTROY,
            regionNames = ["Customers"], types = [EnableExpiration.ExpirationType.TIME_TO_LIVE]),
    EnableExpiration.ExpirationPolicy(timeout = 2, action = ExpirationActionType.DESTROY,
            regionNames = ["Customers"], types = [EnableExpiration.ExpirationType.IDLE_TIMEOUT])])
class CacheDefinedExpirationServerConfigKT {

    @Bean("Customers")
    fun createCustomerRegion(gemFireCache: GemFireCache): ReplicatedRegionFactoryBean<Long, Customer> {
        val regionFactoryBean = ReplicatedRegionFactoryBean<Long, Customer>()
        regionFactoryBean.cache = gemFireCache
        regionFactoryBean.scope = Scope.DISTRIBUTED_ACK
        regionFactoryBean.dataPolicy = DataPolicy.REPLICATE
        regionFactoryBean.setName("Customers")
        regionFactoryBean.isStatisticsEnabled = true
        return regionFactoryBean
    }
}
