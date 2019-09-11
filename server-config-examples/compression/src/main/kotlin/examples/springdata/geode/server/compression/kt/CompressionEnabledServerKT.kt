package examples.springdata.geode.server.compression.kt

import examples.springdata.geode.server.compression.kt.config.CompressionEnabledServerConfigKT
import examples.springdata.geode.server.compression.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.util.createCustomers
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackageClasses = [CompressionEnabledServerConfigKT::class])
class CompressionEnabledServerKT {

    @Bean
    internal fun runner(customerRepository: CustomerRepositoryKT): ApplicationRunner {
        println("Inserting 4000 Customers into compressed region")
        return ApplicationRunner { createCustomers(4000, customerRepository) }
    }
}

fun main(args: Array<String>) {
    SpringApplicationBuilder(CompressionEnabledServerKT::class.java).web(WebApplicationType.NONE).build().run(*args)
}