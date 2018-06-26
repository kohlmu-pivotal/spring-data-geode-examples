package examples.springdata.geode.common.kt.server

import org.apache.geode.cache.CacheFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackageClasses = [ServerApplicationConfigKT::class])
class ServerKT

fun main(args: Array<String>) {
    SpringApplication.run(ServerKT::class.java, *args).apply {
        while (true) {
            val cqs = CacheFactory.getAnyInstance().queryService.cqs
            for (cq in cqs) {
                println(cq.name)
            }
            Thread.sleep(1000)
        }
    }
}