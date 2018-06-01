package org.springframework.data.examples.geode.cq.kt.client

import org.apache.geode.cache.client.PoolManager
import org.springframework.beans.factory.getBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.examples.geode.cq.kt.client.config.consumer.CQConsumerClientApplicationConfigKT

/**
 *
 * @author Udo Kohlmeyer
 */
@SpringBootApplication(scanBasePackageClasses = [CQConsumerClientApplicationConfigKT::class])
class CQConsumerClientKT

fun main(args: Array<String>) {
    SpringApplication.run(CQConsumerClientKT::class.java, *args)?.apply {
        getBean<CQConsumerClientKT>(CQConsumerClientKT::class).apply {
            PoolManager.getAll().forEach { poolName, pool -> println("$poolName - ${pool.subscriptionEnabled}") }
            while (true) {
                Thread.sleep(1000)
            }
        }
    }
}