package examples.springdata.geode.kt.entityregion.server

import examples.springdata.geode.kt.entityregion.server.config.EntityDefinedRegionServerConfigKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
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
    SpringApplicationBuilder(ServerKT::class.java)
        .web(WebApplicationType.NONE)
        .build()
        .run(*args)

}