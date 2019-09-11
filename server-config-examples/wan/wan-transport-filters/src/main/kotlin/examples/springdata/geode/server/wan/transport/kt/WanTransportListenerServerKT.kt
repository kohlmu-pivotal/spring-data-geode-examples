package examples.springdata.geode.server.wan.transport.kt

import examples.springdata.geode.server.wan.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.server.wan.transport.kt.config.WanTransportListenerConfigKT
import examples.springdata.geode.util.createCustomers
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

@SpringBootApplication(scanBasePackageClasses = [WanTransportListenerConfigKT::class])
class WanTransportListenerServerKT {

    @Bean
    @Profile("default", "SiteA")
    fun siteARunner() = ApplicationRunner {
        readLine()
    }

    @Bean
    @Profile("SiteB")
    fun siteBRunner(customerRepositoryKT: CustomerRepositoryKT) = ApplicationRunner {
        println("Inserting 300 customers")
        createCustomers(300, customerRepositoryKT)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(WanTransportListenerServerKT::class.java)
                    .web(WebApplicationType.NONE)
                    .build()
                    .run(*args)
        }
    }
}