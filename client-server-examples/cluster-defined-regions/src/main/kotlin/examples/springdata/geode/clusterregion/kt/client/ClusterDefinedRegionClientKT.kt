package examples.springdata.geode.clusterregion.kt.client

import examples.springdata.geode.client.common.kt.client.BaseClientKT
import examples.springdata.geode.clusterregion.kt.client.config.ClusterDefinedRegionClientConfigKT
import examples.springdata.geode.clusterregion.kt.client.service.CustomerServiceKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackageClasses = [ClusterDefinedRegionClientConfigKT::class])
class ClusterDefinedRegionClientKT(val customerServiceKT: CustomerServiceKT) : BaseClientKT {
    @Bean
    fun runner(): ApplicationRunner = ApplicationRunner {
       populateData(customerServiceKT = customerServiceKT)
    }
}

fun main() {
    SpringApplication.run(ClusterDefinedRegionClientKT::class.java)
}