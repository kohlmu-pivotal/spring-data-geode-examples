package examples.springdata.geode.clusterregion.kt.server

import examples.springdata.geode.clusterregion.kt.server.config.ClusterDefinedRegionServerConfigKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean


@SpringBootApplication(scanBasePackageClasses = [ClusterDefinedRegionServerConfigKT::class])
class ClusterDefinedRegionServerKT {
    @Bean
    fun runner() = ApplicationRunner {
        println("Press <ENTER> to exit")
        readLine()
    }
}

fun main(args: Array<String>) {
    SpringApplicationBuilder(ClusterDefinedRegionServerKT::class.java)
        .web(WebApplicationType.NONE)
        .build()
        .run(*args)

}