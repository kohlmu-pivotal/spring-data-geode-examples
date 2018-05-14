package org.springframework.data.examples.geode.basic.kt.server

import org.apache.geode.cache.CacheListener
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Region
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.examples.geode.domain.Customer
import org.springframework.data.examples.geode.util.LoggingCacheListener
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.CacheServerApplication
import org.springframework.data.gemfire.config.annotation.EnableLocator
import java.util.*
import javax.annotation.Resource

@SpringBootApplication(scanBasePackages = ["org.springframework.data.examples.geode.basic.kt.server"])
@EnableLocator
@CacheServerApplication
class ServerKT {

    @Resource
    internal lateinit var customerRegion: Region<String, Customer>

    @Bean
    internal fun loggingCacheListener() = LoggingCacheListener<String, Customer>() as CacheListener<String, Customer>

    @Bean("customerRegion")
    internal fun customerRegion(gemfireCache: GemFireCache) = ReplicatedRegionFactoryBean<String, Customer>().also {
        it.cache = gemfireCache
        it.setRegionName("Customer")
        it.dataPolicy = DataPolicy.REPLICATE
        it.setCacheListeners(arrayOf(loggingCacheListener()))
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ServerKT::class.java, *args)
    System.err.println("Press <ENTER> to exit")
    Scanner(System.`in`).nextLine()
}