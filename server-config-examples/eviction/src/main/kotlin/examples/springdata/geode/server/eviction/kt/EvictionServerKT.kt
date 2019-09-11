package examples.springdata.geode.server.eviction.kt

import examples.springdata.geode.server.eviction.kt.config.EvictionServerConfigKT
import examples.springdata.geode.server.eviction.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.server.eviction.kt.repo.OrderRepositoryKT
import examples.springdata.geode.server.eviction.kt.repo.ProductRepositoryKT
import examples.springdata.geode.util.createCustomers
import examples.springdata.geode.util.createOrders
import examples.springdata.geode.util.createProducts
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackageClasses = [EvictionServerConfigKT::class])
class EvictionServerKT {
    @Bean
    fun runner(customerRepositoryKT: CustomerRepositoryKT,
               orderRepositoryKT: OrderRepositoryKT,
               productRepositoryKT: ProductRepositoryKT) = ApplicationRunner {
        createCustomerData(customerRepositoryKT)
        createProducts(productRepositoryKT)
        createOrders(productRepositoryKT, orderRepositoryKT)
    }

    private fun createOrders(productRepository: ProductRepositoryKT, orderRepository: OrderRepositoryKT) {
        createOrders(100, 300, 300, 5, orderRepository, productRepository)
    }

    private fun createProducts(productRepository: ProductRepositoryKT) {
        println("Inserting 300 products")
        createProducts(300, productRepository)
    }

    private fun createCustomerData(customerRepository: CustomerRepositoryKT) {
        println("Inserting 300 customers")
        createCustomers(300, customerRepository)
    }
}

fun main(args: Array<String>) {
    SpringApplicationBuilder(EvictionServerKT::class.java).web(WebApplicationType.NONE).build().run(*args)
}