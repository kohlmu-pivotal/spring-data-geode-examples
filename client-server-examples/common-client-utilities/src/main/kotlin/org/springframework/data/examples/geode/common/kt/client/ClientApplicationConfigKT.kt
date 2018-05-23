package org.springframework.data.examples.geode.common.kt.client

import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.data.examples.geode.model.Customer
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.ClientCacheConfiguration
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer
import org.springframework.data.gemfire.support.ConnectionEndpoint

@Configuration
abstract class ClientApplicationConfigKT {
    companion object {
        const val CUSTOMER_REGION_NAME = "Customers"
        const val CUSTOMER_REGION_BEAN_NAME = "customerRegion"
    }

    @Bean(CUSTOMER_REGION_BEAN_NAME)
    @Profile("proxy")
    protected fun configureProxyClientCustomerRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<String, Customer>()
        .apply {
            cache = gemFireCache
            setName(CUSTOMER_REGION_NAME)
            setShortcut(ClientRegionShortcut.PROXY)
        }

    @Bean(CUSTOMER_REGION_BEAN_NAME)
    @Profile("localCache")
    protected fun configureLocalCachingClientCustomerRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<String, Customer>()
        .apply {
            cache = gemFireCache
            setName(CUSTOMER_REGION_NAME)
            setShortcut(ClientRegionShortcut.CACHING_PROXY)
        }

    @ClientCacheApplication(name = "DemoClientCache", logLevel = "error", pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
    protected class BasicClientConfiguration : ClientCacheConfiguration() {

        @Bean
        internal fun clientCacheServerConfigurer(
            @Value("\${spring.data.geode.locator.host:localhost}") hostname: String,
            @Value("\${spring.data.geode.locator.port:10334}") port: Int) =
            ClientCacheConfigurer { _, clientCacheFactoryBean ->
                clientCacheFactoryBean.setLocators(listOf(ConnectionEndpoint(hostname, port)))
            }

        companion object {
            // Required to resolve property placeholders in Spring @Value annotations.
            @Bean
            internal fun propertyPlaceholderConfigurer() = PropertySourcesPlaceholderConfigurer()
        }
    }
}