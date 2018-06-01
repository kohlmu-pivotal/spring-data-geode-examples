package org.springframework.data.examples.geode.cq.kt.client.config.consumer

import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientRegionShortcut
import org.apache.geode.cache.query.CqEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.*
import org.springframework.data.examples.geode.cq.kt.client.repo.CustomerRepositoryKT
import org.springframework.data.examples.geode.cq.kt.client.services.CustomerServiceKT
import org.springframework.data.examples.geode.model.Customer
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer
import org.springframework.data.gemfire.config.annotation.EnableContinuousQueries
import org.springframework.data.gemfire.config.annotation.EnablePool
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.gemfire.support.ConnectionEndpoint

@Configuration
@ComponentScan(basePackageClasses = [CustomerServiceKT::class])
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@EnableContinuousQueries(poolName = "cqPool")
@EnablePool(name = "cqPool", subscriptionEnabled = true)
@ClientCacheApplication(name = "CQConsumerClientCache", logLevel = "info", pingInterval = 5000L, readTimeout = 15000,
    retryAttempts = 1, subscriptionEnabled = true)
class CQConsumerClientApplicationConfigKT {

    @ContinuousQuery(name = "CustomerJudeCQ", query = "SELECT * FROM /Customers")
    @DependsOn("Customers")
    fun customerNameJudeCQ(cqEvent: CqEvent) {
        println("Received message for CQ 'CustomerJudeCQ': ${cqEvent.newValue}")
    }

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