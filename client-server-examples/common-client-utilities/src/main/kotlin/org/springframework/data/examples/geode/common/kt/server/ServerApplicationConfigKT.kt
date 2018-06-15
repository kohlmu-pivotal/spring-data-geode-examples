package org.springframework.data.examples.geode.common.kt.server

import org.apache.geode.cache.CacheListener
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Scope
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.data.examples.geode.model.Customer
import org.springframework.data.examples.geode.util.LoggingCacheListener
import org.springframework.data.gemfire.IndexFactoryBean
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.CacheServerApplication
import org.springframework.data.gemfire.config.annotation.EnableLocator

/**
 * The server application configuration file. This configuration file creates: LoggingCacheListener, CustomerRegion,
 * CacheServer and starts a Locator on the default host:localhost and port 10334. Which the server will use to join
 * the cluster and the client to connect to the locator to receive a connection to a registered server.
 */

@Configuration
@EnableLocator
@CacheServerApplication(port = 0)
open class ServerApplicationConfigKT(protected val applicationContext: ApplicationContext) {

    @Bean("loggingCacheListener")
    internal open fun loggingCacheListener() = LoggingCacheListener<Any, Any>()

    private fun loggingCustomerCacheListener(): CacheListener<Long, Customer> =
        applicationContext.getBean("loggingCacheListener") as CacheListener<Long, Customer>

    @Bean("Customers")
    protected open fun customerRegion(gemfireCache: GemFireCache) =
        ReplicatedRegionFactoryBean<Long, Customer>().apply {
            cache = gemfireCache
            setRegionName("Customers")
            scope = Scope.DISTRIBUTED_ACK
            dataPolicy = DataPolicy.REPLICATE
            setCacheListeners(arrayOf(loggingCustomerCacheListener()))
        }

    @Bean("FirstNameIndex")
    @DependsOn("Customers")
    protected open fun createFirstNameIndex(gemFireCache: GemFireCache) =
        IndexFactoryBean().apply {
            setCache(gemFireCache)
            setDefine(false)
            setName("firstNameIndex")
            setExpression("firstName")
            setFrom("/Customers")
        }

    @Bean("EmailAddressIndex")
    @DependsOn("Customers")
    protected open fun createEmailAddressIndex(gemFireCache: GemFireCache) =
        IndexFactoryBean().apply {
            setCache(gemFireCache)
            setDefine(false)
            setName("emailAddressIndex")
            setExpression("emailAddress.value")
            setFrom("/Customers")
        }
}