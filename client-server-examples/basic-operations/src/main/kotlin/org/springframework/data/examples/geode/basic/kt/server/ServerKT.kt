package org.springframework.data.examples.geode.basic.kt.server

import org.apache.geode.cache.Region
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.examples.geode.domain.Customer
import org.springframework.data.gemfire.config.annotation.CacheServerApplication
import org.springframework.data.gemfire.config.annotation.EnableLocator
import javax.annotation.Resource

@SpringBootApplication(scanBasePackages = ["org.springframework.data.examples.geode.basic.kt.server"])
@EnableLocator
@CacheServerApplication
class ServerKT {

    @Resource
    internal lateinit var customerRegion: Region<String, Customer>
}

fun main(args: Array<String>) {
    SpringApplication.run(ServerKT::class.java, *args)
    System.err.println("Press <ENTER> to exit")
    readLine()
}