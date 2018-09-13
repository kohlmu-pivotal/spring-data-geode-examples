package examples.springdata.geode.client.cq.kt.producer.config

import examples.springdata.geode.client.cq.kt.producer.repo.CustomerRepositoryKT
import examples.springdata.geode.client.cq.kt.producer.services.CustomerServiceKT
import examples.springdata.geode.domain.Customer
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Profile
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.gemfire.support.ConnectionEndpoint

@ComponentScan(basePackageClasses = [CustomerServiceKT::class])
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@ClientCacheApplication(name = "CQProducerClientCache", logLevel = "info", pingInterval = 5000L, readTimeout = 15000,
        retryAttempts = 1, subscriptionEnabled = false)
class CQProducerClientApplicationConfigKT {

    @Bean("Customers")
    @Profile("proxy", "default")
    protected fun configureProxyClientCustomerRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<Long, Customer>()
            .apply {
                cache = gemFireCache
                setName("Customers")
                setShortcut(ClientRegionShortcut.PROXY)
            }

    @Bean
    internal fun clientCacheServerConfigurer(
            @Value("\${spring.data.geode.locator.host:localhost}") hostname: String,
            @Value("\${spring.data.geode.locator.port:10334}") port: Int) =
            ClientCacheConfigurer { _, clientCacheFactoryBean ->
                clientCacheFactoryBean.setLocators(listOf(ConnectionEndpoint(hostname, port)))
            }
}

