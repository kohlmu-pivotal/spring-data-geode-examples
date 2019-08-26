package examples.springdata.geode.server.wan.kt.client

import examples.springdata.geode.client.common.kt.client.BaseClientKT
import examples.springdata.geode.server.wan.kt.client.config.WanClientConfigKT
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackageClasses = [WanClientConfigKT::class])
class WanClientKT : BaseClientKT {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(WanClientKT::class.java, *args)
        }
    }
}