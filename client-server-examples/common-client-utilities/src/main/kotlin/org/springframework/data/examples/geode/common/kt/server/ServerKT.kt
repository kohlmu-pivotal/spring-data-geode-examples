package org.springframework.data.examples.geode.common.kt.server

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackageClasses = [ServerApplicationConfigKT::class])
class ServerKT

fun main(args: Array<String>) {
    SpringApplication.run(ServerKT::class.java, *args)
    System.err.println("Press <ENTER> to exit")
    readLine()
}