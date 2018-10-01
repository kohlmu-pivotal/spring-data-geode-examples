package examples.springdata.geode.kt.entityregion.entityregion.client

import examples.springdata.geode.client.common.kt.client.BaseClientKT
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.EmailAddress
import examples.springdata.geode.kt.entityregion.client.config.EntityDefinedRegionClientConfigKT
import examples.springdata.geode.kt.entityregion.entityregion.client.service.CustomerServiceKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackageClasses = [EntityDefinedRegionClientConfigKT::class])
class EntityDefinedRegionClientKT(val customerServiceKT: CustomerServiceKT) : BaseClientKT {
    @Bean
    fun runner(): ApplicationRunner = ApplicationRunner {
       populateData(customerServiceKT = customerServiceKT)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(EntityDefinedRegionClientKT::class.java)
}