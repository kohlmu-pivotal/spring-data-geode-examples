package examples.springdata.geode.client.common.kt.server.config

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.util.LoggingCacheListener
import org.apache.geode.cache.CacheListener
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Scope
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Profile
import org.springframework.data.gemfire.IndexFactoryBean
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.CacheServerApplication
import org.springframework.data.gemfire.config.annotation.EnableLocator

/**
 * The server application configuration file. This configuration file creates: LoggingCacheListener, CustomerRegion,
 * CacheServer and starts a Locator on the default host:localhost and port 10334. Which the server will use to join
 * the cluster and the client to connect to the locator to receive a connection to a registered server.
 */

@Profile("default")
@EnableLocator
@CacheServerApplication(port = 0, logLevel = "info")
class ServerApplicationConfigKT {

    @Bean("loggingCacheListener")
    @Profile("default")
    internal fun loggingCacheListener() = LoggingCacheListener<Any, Any>()

    @Bean("Customers")
    @Profile("default")
    protected open fun customerRegion(gemfireCache: GemFireCache) =
            ReplicatedRegionFactoryBean<Long, Customer>().apply {
                cache = gemfireCache
                setRegionName("Customers")
                scope = Scope.DISTRIBUTED_ACK
                dataPolicy = DataPolicy.REPLICATE
                setCacheListeners(arrayOf(loggingCacheListener() as CacheListener<Long, Customer>))
            }

    @Bean("FirstNameIndex")
    @DependsOn("Customers")
    @Profile("default")
    protected fun createFirstNameIndex(gemFireCache: GemFireCache) =
            IndexFactoryBean().apply {
                setCache(gemFireCache)
                setDefine(false)
                setName("firstNameIndex")
                setExpression("firstName")
                setFrom("/Customers")
            }

    @Bean("EmailAddressIndex")
    @DependsOn("Customers")
    @Profile("default")
    protected fun createEmailAddressIndex(gemFireCache: GemFireCache) =
            IndexFactoryBean().apply {
                setCache(gemFireCache)
                setDefine(false)
                setName("emailAddressIndex")
                setExpression("emailAddress.value")
                setFrom("/Customers")
            }
}