package examples.springdata.geode.client.common.kt.server.config

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.util.LoggingCacheListener
import org.apache.geode.cache.CacheListener
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Scope
import org.springframework.context.annotation.Bean
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.CacheServerApplication
import org.springframework.data.gemfire.config.annotation.EnableIndexing
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.EnableManager

/**
 * The server application configuration file. This configuration file creates: LoggingCacheListener, CustomerRegion,
 * CacheServer and starts a Locator on the default host:localhost and port 10334. Which the server will use to join
 * the cluster and the client to connect to the locator to receive a connection to a registered server.
 */

@EnableLocator
@EnableIndexing
@EnableManager
@CacheServerApplication(port = 0, logLevel = "error")
class ServerApplicationConfigKT {

    @Bean
    internal fun loggingCacheListener() = LoggingCacheListener<Any, Any>()

    @Bean
    protected fun customerRegion(gemfireCache: GemFireCache) =
            ReplicatedRegionFactoryBean<Long, Customer>().apply {
                cache = gemfireCache
                setRegionName("Customers")
                scope = Scope.DISTRIBUTED_ACK
                dataPolicy = DataPolicy.REPLICATE
                setCacheListeners(arrayOf(loggingCacheListener() as CacheListener<Long, Customer>))
            }
}

