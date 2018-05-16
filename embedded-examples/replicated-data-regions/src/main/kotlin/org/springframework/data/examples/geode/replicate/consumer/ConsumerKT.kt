package org.springframework.data.examples.geode.replicate.consumer

import org.apache.geode.cache.CacheListener
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Region
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.examples.geode.model.Customer
import org.springframework.data.examples.geode.util.LoggingCacheListener
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import javax.annotation.Resource

@SpringBootApplication
@PeerCacheApplication(name = "ConsumerPeer")
@EnableLocator
@EnableEntityDefinedRegions(basePackageClasses = [Customer::class])
class ConsumerKT {

    @Resource
    private lateinit var customerRegion: Region<String, Customer>

    @Bean
    internal fun loggingCacheListener() = LoggingCacheListener<String, Customer>() as CacheListener<String, Customer>

    @Bean
    fun customerRegion(gemfireCache: GemFireCache) =
        ReplicatedRegionFactoryBean<String, Customer>().apply {
            cache = gemfireCache
            setRegionName("Customers")
            setCacheListeners(arrayOf(loggingCacheListener()))
            dataPolicy = DataPolicy.REPLICATE
        }
}


fun main(args: Array<String>) {
    SpringApplication.run(ConsumerKT::class.java, *args)
    println("Press <ENTER> to exit")
    readLine()
}