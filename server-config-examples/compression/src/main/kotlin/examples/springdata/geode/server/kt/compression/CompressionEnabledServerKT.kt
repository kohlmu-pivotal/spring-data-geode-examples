package examples.springdata.geode.server.kt.compression

import examples.springdata.geode.server.kt.compression.config.CompressionEnabledServerConfigKT
import examples.springdata.geode.server.kt.compression.repo.CustomerRepoKT
import examples.springdata.geode.util.createCustomers
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackageClasses = [CompressionEnabledServerConfigKT::class])
class CompressionEnabledServerKT {

    @Bean
    internal fun runner(customerRepo: CustomerRepoKT): ApplicationRunner {
        return ApplicationRunner { createCustomers(4000, customerRepo) }
    }
}

fun main(args: Array<String>) {
    SpringApplicationBuilder(CompressionEnabledServerKT::class.java).web(WebApplicationType.NONE).build().run(*args)
}