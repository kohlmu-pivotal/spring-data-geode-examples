package examples.springdata.geode.client.common.kt.server

import examples.springdata.geode.client.common.kt.server.config.ServerApplicationConfigKT
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackageClasses = [ServerApplicationConfigKT::class])
class ServerKT

fun main(args: Array<String>) {
    SpringApplication.run(ServerKT::class.java, *args)
    println("Press <ENTER> to exit")
    readLine()
}