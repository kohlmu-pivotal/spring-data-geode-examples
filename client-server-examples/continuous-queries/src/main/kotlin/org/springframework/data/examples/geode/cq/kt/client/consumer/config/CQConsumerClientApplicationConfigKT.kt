package org.springframework.data.examples.geode.cq.kt.client.consumer.config

import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientRegionShortcut
import org.apache.geode.cache.query.CqEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.examples.geode.model.Customer
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery

@Configuration
class CQConsumerClientApplicationConfigKT {

    @ContinuousQuery(name = "CustomerJudeCQ", query = "SELECT * FROM /Customers")
    fun customerNameJudeCQ(cqEvent: CqEvent) {
        println("Received message for CQ 'CustomerJudeCQ': ${cqEvent.newValue}")
    }

    @Bean("Customers")
    @Profile("proxy", "default")
    protected open fun configureProxyClientCustomerRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<Long, Customer>()
        .apply {
            cache = gemFireCache
            setName("Customers")
            setShortcut(ClientRegionShortcut.PROXY)
        }
//
//    @Bean
//    internal fun clientCacheServerConfigurer(
//        @Value("\${spring.data.geode.locator.host:localhost}") hostname: String,
//        @Value("\${spring.data.geode.locator.port:10334}") port: Int) =
//        ClientCacheConfigurer { _, clientCacheFactoryBean ->
//            clientCacheFactoryBean.setLocators(listOf(ConnectionEndpoint(hostname, port)))
//        }
}