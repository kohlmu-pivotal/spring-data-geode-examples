package examples.springdata.geode.client.serialization.kt.server.config

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.util.LoggingCacheListener
import org.apache.geode.cache.CacheListener
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Scope
import org.apache.geode.pdx.PdxInstance
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.CacheServerApplication
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.EnablePdx

/**
 * The server application configuration file. This configuration file creates: LoggingCacheListener, CustomerRegion,
 * CacheServer and starts a Locator on the default host:localhost and port 10334. Which the server will use to join
 * the cluster and the client to connect to the locator to receive a connection to a registered server.
 */

@Profile("default")
@EnableLocator
@EnablePdx
@CacheServerApplication(port = 0, logLevel = "error")
class ServerApplicationConfigKT {

    @Bean("loggingCacheListener")
    @Profile("default")
    internal fun loggingCacheListener() = LoggingCacheListener<Long, Customer>()

    @Bean("Customers")
    @Profile("default")
    protected fun customerRegion(gemfireCache: GemFireCache) =
            ReplicatedRegionFactoryBean<Long, Customer>().apply {
                cache = gemfireCache
                setRegionName("Customers")
                scope = Scope.DISTRIBUTED_ACK
                dataPolicy = DataPolicy.REPLICATE
                setCacheListeners(arrayOf(loggingCacheListener() as CacheListener<Long, Customer>))
            }
}

@Profile("readSerialized")
@EnableLocator
@EnablePdx(readSerialized = true)
@CacheServerApplication(port = 0, logLevel = "error")
class ReadSerializedServerApplicationConfigKT {

    @Bean("loggingCacheListener")
    @Profile("readSerialized")
    internal fun loggingCacheListener() = LoggingCacheListener<Long, PdxInstance>()

    @Bean("Customers")
    @Profile("readSerialized")
    protected fun customerRegion(gemfireCache: GemFireCache) =
        ReplicatedRegionFactoryBean<Long, PdxInstance>().apply {
            cache = gemfireCache
            setRegionName("Customers")
            scope = Scope.DISTRIBUTED_ACK
            dataPolicy = DataPolicy.REPLICATE
            setCacheListeners(arrayOf(loggingCacheListener() as CacheListener<Long, PdxInstance>))
        }
}