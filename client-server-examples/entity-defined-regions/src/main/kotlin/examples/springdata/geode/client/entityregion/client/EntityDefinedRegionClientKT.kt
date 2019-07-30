package examples.springdata.geode.client.entityregion.client

import examples.springdata.geode.client.common.kt.client.BaseClientKT
import examples.springdata.geode.client.entityregion.client.config.EntityDefinedRegionClientConfigKT
import examples.springdata.geode.client.entityregion.client.service.CustomerServiceKT
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

fun main() {
    SpringApplication.run(EntityDefinedRegionClientKT::class.java)
}