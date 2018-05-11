package org.springframework.data.examples.geode.replicate.client

import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Region
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.getBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.data.examples.geode.domain.Customer
import org.springframework.data.examples.geode.domain.EmailAddress
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.ClientCacheConfiguration
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer
import javax.annotation.Resource

@SpringBootApplication
class ClientKT {

    @Resource
    internal lateinit var clientCustomerRegion: Region<String, Customer>

    @ClientCacheApplication(name = "ReplicateRegionClient", logLevel = "error", pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
    internal class ReplicateClientCacheConfiguration : ClientCacheConfiguration() {

        @Bean
        internal fun clientCacheServerConfigurer(
            @Value("\${spring.session.data.geode.cache.server.host:localhost}") hostname: String,
            @Value("\${spring.session.data.geode.cache.server.port:40404}") port: Int) =
            ClientCacheConfigurer { _, clientCacheFactoryBean ->
                clientCacheFactoryBean.setServers(listOf(newConnectionEndpoint(hostname, port)))
            }

        // Required to resolve property placeholders in Spring @Value annotations.
        @Bean
        internal fun propertyPlaceholderConfigurer() = PropertySourcesPlaceholderConfigurer()
    }

    @Bean("clientCustomerRegion")
    internal fun configureClientCustomerRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<String, Customer>()
        .apply {
            cache = gemFireCache
            setName("customerRegion")
            setShortcut(ClientRegionShortcut.PROXY)
        }
}

fun main(args: Array<String>) {
    SpringApplication.run(ClientKT::class.java, *args).apply {
        getBean<Region<String, Customer>>("clientCustomerRegion").also {
            it["1"] = Customer(123, EmailAddress("2@2.com"), "Me", "My")
            it["2"] = Customer(1234, EmailAddress("3@3.com"), "You", "Yours")
        }
    }
}
