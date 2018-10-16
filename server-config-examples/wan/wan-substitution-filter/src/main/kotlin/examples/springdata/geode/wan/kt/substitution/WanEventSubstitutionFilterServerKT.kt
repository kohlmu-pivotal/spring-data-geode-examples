package examples.springdata.geode.wan.kt.substitution

import examples.springdata.geode.server.wan.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.util.createCustomers
import examples.springdata.geode.wan.kt.substitution.config.WanEventSubstitutionFilterConfigKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

@SpringBootApplication(scanBasePackageClasses = [WanEventSubstitutionFilterConfigKT::class])
class WanEventSubstitutionFilterServerKT {

    @Bean
    @Profile("default", "SiteA")
    fun siteARunner() =
        ApplicationRunner {
            readLine()
        }

    @Bean
    @Profile("SiteB")
    fun siteBRunner(customerRepositoryKT: CustomerRepositoryKT) =
        ApplicationRunner {
            println("Inserting 300 customers")
            createCustomers(300, customerRepositoryKT)
        }

}

fun main(args: Array<String>) {
    SpringApplicationBuilder(WanEventSubstitutionFilterServerKT::class.java)
        .web(WebApplicationType.NONE)
        .build()
        .run(*args)
}