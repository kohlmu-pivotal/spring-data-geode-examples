package examples.springdata.geode.server.wan.kt

import examples.springdata.geode.server.wan.config.WanEnableServerConfig
import examples.springdata.geode.server.wan.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.util.createCustomers
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

@SpringBootApplication(scanBasePackageClasses = [WanEnableServerConfig::class])
class WanEnableServerKT {

    @Bean
    @Profile("default", "SiteA")
    fun siteBRunner() =
            ApplicationRunner {
                readLine()
            }

    @Bean
    @Profile("SiteB")
    fun siteARunner(customerRepository: CustomerRepositoryKT) =
            ApplicationRunner {
                println("Inserting 300 customers")
                createCustomers(3, customerRepository)
            }
}

fun main(args: Array<String>) {
    SpringApplicationBuilder(WanEnableServerKT::class.java)
            .web(WebApplicationType.NONE)
            .build()
            .run(*args)
}