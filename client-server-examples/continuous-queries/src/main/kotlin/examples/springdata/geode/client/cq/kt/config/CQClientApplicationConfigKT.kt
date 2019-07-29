package examples.springdata.geode.client.cq.kt.config

import examples.springdata.geode.client.cq.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.client.cq.kt.services.CustomerServiceKT
import examples.springdata.geode.domain.Customer
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableContinuousQueries
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@ComponentScan(basePackageClasses = [CustomerServiceKT::class])
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@ClientCacheApplication(name = "CQClientCache", logLevel = "error", pingInterval = 5000L, readTimeout = 15000,
        retryAttempts = 1, subscriptionEnabled = true, readyForEvents = true)
@EnableContinuousQueries
class CQClientApplicationConfigKT {

    @Bean("Customers")
    protected fun configureProxyClientCustomerRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<Long, Customer>()
            .apply {
                cache = gemFireCache
                setName("Customers")
                setShortcut(ClientRegionShortcut.PROXY)
            }
}