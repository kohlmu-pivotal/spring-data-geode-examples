package examples.springdata.geode.functions.cascading.kt.server

import examples.springdata.geode.functions.cascading.kt.server.config.ServerCascadingFunctionServerConfigKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.data.gemfire.config.annotation.EnableManager
import java.util.*

@SpringBootApplication
@EnableManager(start = true)
@ComponentScan(basePackages = ["examples.springdata.geode.functions.cascading.kt.server.functions"])
@Import(ServerCascadingFunctionServerConfigKT::class)
class CascadingFunctionServerKT {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val springApplication = SpringApplicationBuilder(CascadingFunctionServerKT::class.java)
                    .web(WebApplicationType.NONE)
                    .build()
            val profile = if (args.isNotEmpty()) {
                args[0]
            } else {
                "default"
            }
            springApplication.setAdditionalProfiles(profile)
            springApplication.run(*args)
        }
    }

    @Bean
    fun runner() = ApplicationRunner {
        Scanner(System.`in`).nextLine()
    }
}