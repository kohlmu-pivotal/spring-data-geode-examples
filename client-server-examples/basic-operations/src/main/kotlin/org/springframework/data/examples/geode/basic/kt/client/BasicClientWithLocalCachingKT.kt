package org.springframework.data.examples.geode.basic.kt.client

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

private const val CUSTOMER_REGION_BEAN_NAME = "clientCustomerRegion"
private const val CUSTOMER_REGION_NAME = "customerRegion"

@SpringBootApplication(scanBasePackages = ["org.springframework.data.examples.geode.basic.kt.client"])
class BasicClientWithLocalCachingKT {

    @Resource
    internal lateinit var clientCustomerRegion: Region<String, Customer>

    @ClientCacheApplication(name = "BasicClientWithLocalCachingKT", logLevel = "error", pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
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

    @Bean(CUSTOMER_REGION_BEAN_NAME)
    internal fun configureClientCustomerRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<String, Customer>()
        .apply {
            cache = gemFireCache
            setName(CUSTOMER_REGION_NAME)
            setShortcut(ClientRegionShortcut.CACHING_PROXY)
        }
}

fun main(args: Array<String>) {
    SpringApplication.run(BasicClientWithLocalCachingKT::class.java, *args).apply {
        getBean<Region<String, Customer>>(CUSTOMER_REGION_BEAN_NAME).also {
            println("Inserting 3 entries for keys: \"1\", \"2\",\"3\"")
            it["1"] = Customer(123, EmailAddress("2@2.com"), "Me", "My")
            it["2"] = Customer(1234, EmailAddress("3@3.com"), "You", "Yours")
            it["3"] = Customer(9876, EmailAddress("5@5.com"), "Third", "Entry")

            println("Entries on Client: ${it.size}")
            println("Entries on Server: ${it.keySetOnServer().size}")
            it.keySetOnServer().forEach { entry -> println("\t Entry: \n \t\t Key: $entry \n \t\t Value: ${it[entry]}") }

            println("Updating entry for key: \"2\"")
            println("Entry Before: ${it["2"]}")
            it["2"] = Customer(456, EmailAddress("4@4.com"), "First", "Update")
            println("Entry After: ${it["2"]}")

            println("Removing 1 entry for key: \"3\"")
            it.remove("3")

            println("Entries:")
            it.keySetOnServer().forEach { entry -> println("\t Entry: \n \t\t Key: $entry \n \t\t Value: ${it[entry]}") }
        }
    }
}
