package examples.springdata.geode.kt.client.serialization.client

import examples.springdata.geode.client.common.kt.client.BaseClientKT
import examples.springdata.geode.client.common.kt.client.service.CustomerServiceKT
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.kt.client.serialization.client.config.PdxSerializationClientConfigKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackageClasses = [PdxSerializationClientConfigKT::class])
class SerializationClientKT : BaseClientKT {
    @Bean
    fun runner(customerServiceKT: CustomerServiceKT<Customer>) = ApplicationRunner {
        populateData(customerServiceKT)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(SerializationClientKT::class.java, *args)
}