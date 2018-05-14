package org.springframework.data.examples.geode.basic.kt.client

import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Region
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.getBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.data.examples.geode.basic.kt.repository.CustomerRepositoryKT
import org.springframework.data.examples.geode.domain.Customer
import org.springframework.data.examples.geode.domain.EmailAddress
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.ClientCacheConfiguration
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import javax.annotation.Resource

private const val CUSTOMER_REGION_BEAN_NAME = "clientCustomerRegion"
private const val CUSTOMER_REGION_NAME = "Customer"

@SpringBootApplication(scanBasePackages = ["org.springframework.data.examples.geode.basic.kt.client.*"])
@EnableGemfireRepositories(basePackages = ["org.springframework.data.examples.geode.basic.kt.repository"])
class BasicClientKT {

    @Resource
    @Qualifier(CUSTOMER_REGION_BEAN_NAME)
    internal lateinit var clientCustomerRegion: Region<String, Customer>

    @Autowired
    internal lateinit var customerRepository: CustomerRepositoryKT

    @ClientCacheApplication(name = "BasicClientKT", logLevel = "error", pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
    @Configuration
    internal class BasicClientConfiguration : ClientCacheConfiguration() {

        @Bean
        internal fun clientCacheServerConfigurer(
            @Value("\${spring.session.data.geode.cache.server.host:localhost}") hostname: String,
            @Value("\${spring.session.data.geode.cache.server.port:40404}") port: Int) =
            ClientCacheConfigurer { _, clientCacheFactoryBean ->
                clientCacheFactoryBean.setServers(listOf(newConnectionEndpoint(hostname, port)))
            }

        companion object {
            // Required to resolve property placeholders in Spring @Value annotations.
            @Bean
            internal fun propertyPlaceholderConfigurer() = PropertySourcesPlaceholderConfigurer()
        }
    }

    @Bean(CUSTOMER_REGION_BEAN_NAME)
    @Profile("proxy")
    internal fun configureProxyClientCustomerRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<String, Customer>()
        .apply {
            cache = gemFireCache
            setName(CUSTOMER_REGION_NAME)
            setShortcut(ClientRegionShortcut.PROXY)
        }


    @Bean(CUSTOMER_REGION_BEAN_NAME)
    @Profile("localCache")
    internal fun configureLocalCachingClientCustomerRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<String, Customer>()
        .apply {
            cache = gemFireCache
            setName(CUSTOMER_REGION_NAME)
            setShortcut(ClientRegionShortcut.CACHING_PROXY)
        }
}

fun main(args: Array<String>) {
    SpringApplication.run(BasicClientKT::class.java, *args)?.apply {
        getBean<BasicClientKT>(BasicClientKT::class).also {
            println("Inserting 3 entries for keys: 1, 2,3")
            it.customerRepository.save(Customer(1, EmailAddress("2@2.com"), "Me", "My"))
            it.customerRepository.save(Customer(2, EmailAddress("3@3.com"), "You", "Yours"))
            it.customerRepository.save(Customer(3, EmailAddress("5@5.com"), "Third", "Entry"))

            println("Entries on Client: ${it.clientCustomerRegion.size}")
            println("Entries on Server: ${it.clientCustomerRegion.keySetOnServer().size}")
            it.customerRepository.findAll().forEach { entry -> println("\t Customer: \n \t\t $entry") }

            println("Updating entry for key: 2")
            println("Entry Before: ${it.customerRepository.findById(2).get()}")
            it.customerRepository.save(Customer(2, EmailAddress("4@4.com"), "First", "Update"))
            println("Entry After: ${it.customerRepository.findById(2).get()}")

            println("Removing 1 entry for key: 3")
            it.customerRepository.deleteById(3)

            println("Entries:")
            it.customerRepository.findAll().forEach { entry -> println("\t Customer: \n \t\t $entry") }
        }
    }
}

