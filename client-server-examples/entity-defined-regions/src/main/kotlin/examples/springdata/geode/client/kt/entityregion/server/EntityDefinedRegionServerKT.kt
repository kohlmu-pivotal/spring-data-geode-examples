package examples.springdata.geode.client.kt.entityregion.server

import examples.springdata.geode.client.kt.entityregion.server.config.EntityDefinedRegionServerConfigKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackageClasses = [EntityDefinedRegionServerConfigKT::class])
class EntityDefinedRegionServerKT {

    @Bean
    internal fun runner() = ApplicationRunner {
        println("Press <ENTER> to exit")
        readLine()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(EntityDefinedRegionServerKT::class.java)
                    .web(WebApplicationType.NONE)
                    .build()
                    .run(*args)
        }
    }
}