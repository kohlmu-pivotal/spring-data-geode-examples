package examples.springdata.geode.client.serialization.kt.server

import examples.springdata.geode.client.serialization.kt.server.config.ServerApplicationConfigKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackageClasses = [ServerApplicationConfigKT::class])
class SerializationServerKT {
    @Bean
    fun runner() = ApplicationRunner {
        println("Press <ENTER> to exit")
        readLine()
    }
}

fun main(args: Array<String>) {
    SpringApplicationBuilder(SerializationServerKT::class.java)
        .web(WebApplicationType.NONE)
        .profiles("readSerialized")
        .build()
        .run(*args)
}