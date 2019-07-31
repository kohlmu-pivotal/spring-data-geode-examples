package examples.springdata.geode.functions.cascading.server

import examples.springdata.geode.functions.cascading.server.config.LocatorCascadingFunctionServerConfigKT
import examples.springdata.geode.functions.cascading.server.config.ServerCascadingFunctionServerConfigKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.data.gemfire.config.annotation.EnableManager

@SpringBootApplication
@EnableManager(start = true)
@ComponentScan(basePackages = ["examples.springdata.geode.functions.cascading.server.functions"])
@Import(ServerCascadingFunctionServerConfigKT::class, LocatorCascadingFunctionServerConfigKT::class)
class CascadingFunctionServerKT {
    @Bean
    fun runner() = ApplicationRunner {
        println("Press <ENTER> to exit")
        readLine()
    }
}

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