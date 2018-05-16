package org.springframework.data.examples.geode.replicate.producer

import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Region
import org.springframework.beans.factory.getBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.examples.geode.model.Customer
import org.springframework.data.examples.geode.model.EmailAddress
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import javax.annotation.Resource

@SpringBootApplication
@PeerCacheApplication(locators = "localhost[10334]", name = "ProducerPeer", logLevel = "warn")
class ProducerKT {

    @Resource
    internal lateinit var customerRegion: Region<String, Customer>

    @Bean("customerRegion")
    internal fun customerRegion(gemfireCache: GemFireCache) =
        ReplicatedRegionFactoryBean<String, Customer>().apply {
            cache = gemfireCache
            setRegionName("customerRegion")
            dataPolicy = DataPolicy.EMPTY
        }
}

fun main(args: Array<String>) {
    SpringApplication.run(ProducerKT::class.java, *args).apply {
        getBean<Region<String, Customer>>("customerRegion").let {
            it["1"] = Customer(123L, EmailAddress("2@2.com"), "Me", "My")
            it["2"] = Customer(1234L, EmailAddress("3@3.com"), "You", "Yours")
        }
    }
}
