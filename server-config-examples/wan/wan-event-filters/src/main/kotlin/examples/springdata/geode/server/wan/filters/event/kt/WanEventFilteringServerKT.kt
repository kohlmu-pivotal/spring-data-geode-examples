package examples.springdata.geode.server.wan.filters.event.kt

import examples.springdata.geode.server.wan.filters.event.kt.config.WanEventFiltersConfigKT
import examples.springdata.geode.server.wan.kt.server.repo.CustomerRepositoryKT
import examples.springdata.geode.util.createCustomers
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

@SpringBootApplication(scanBasePackageClasses = [WanEventFiltersConfigKT::class])
class WanEventFilteringServerKT {

    @Bean
    @Profile("default", "SiteA")
    fun siteARunner() =
            ApplicationRunner {
                readLine()
            }

    @Bean
    @Profile("SiteB")
    fun siteBRunner(customerRepository: CustomerRepositoryKT) =
            ApplicationRunner {
                println("Inserting 300 customers")
                createCustomers(3, customerRepository)
            }

}

fun main(args: Array<String>) {
    SpringApplicationBuilder(WanEventFilteringServerKT::class.java)
            .web(WebApplicationType.NONE)
            .build()
            .run(*args)
}
