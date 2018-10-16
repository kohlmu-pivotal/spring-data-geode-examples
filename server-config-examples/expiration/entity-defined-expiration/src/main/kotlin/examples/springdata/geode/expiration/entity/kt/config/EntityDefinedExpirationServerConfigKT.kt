package examples.springdata.geode.expiration.entity.kt.config

import examples.springdata.geode.expiration.entity.kt.domain.CustomerKT
import examples.springdata.geode.expiration.entity.kt.repo.CustomerRepositoryKT
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Scope
import org.springframework.context.annotation.Bean
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.EnableExpiration
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@PeerCacheApplication
@EnableLocator
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@EnableExpiration
class EntityDefinedExpirationServerConfigKT {

    @Bean("Customers")
    fun createCustomerRegion(gemFireCache: GemFireCache): ReplicatedRegionFactoryBean<Long, CustomerKT> {
        val regionFactoryBean = ReplicatedRegionFactoryBean<Long, CustomerKT>()
        regionFactoryBean.cache = gemFireCache
        regionFactoryBean.scope = Scope.DISTRIBUTED_ACK
        regionFactoryBean.dataPolicy = DataPolicy.REPLICATE
        regionFactoryBean.setName("Customers")
        regionFactoryBean.isStatisticsEnabled = true
        return regionFactoryBean
    }
}
