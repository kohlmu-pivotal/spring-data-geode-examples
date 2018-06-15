package org.springframework.data.examples.geode.cq.kt.client.producer.config

import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.examples.geode.cq.kt.client.producer.repo.CustomerRepositoryKT
import org.springframework.data.examples.geode.cq.kt.client.producer.services.CustomerServiceKT
import org.springframework.data.examples.geode.model.Customer
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.gemfire.support.ConnectionEndpoint

@Configuration
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

