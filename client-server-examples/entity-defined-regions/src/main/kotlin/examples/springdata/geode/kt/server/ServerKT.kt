package examples.springdata.geode.kt.server

import examples.springdata.geode.kt.server.config.EntityDefinedRegionServerConfigKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication(scanBasePackageClasses = [EntityDefinedRegionServerConfigKT::class])
class ServerKT {
    @Bean
    fun runner() = ApplicationRunner {
        println("Press <ENTER> to exit")
        readLine()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ServerKT::class.java, *args)

}