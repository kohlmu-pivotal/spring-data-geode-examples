package examples.springdata.geode.server.wan.kt.server.siteB

import examples.springdata.geode.server.wan.kt.server.siteB.config.WanEnabledServerSiteBConfigKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import java.util.*

@SpringBootApplication(scanBasePackageClasses = [WanEnabledServerSiteBConfigKT::class])
class WanEnabledServerSiteBKT {

    @Bean
    fun siteBRunner() = ApplicationRunner {
        Scanner(System.`in`).nextLine()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(WanEnabledServerSiteBKT::class.java)
                    .web(WebApplicationType.NONE)
                    .build()
                    .run(*args)
        }
    }
}