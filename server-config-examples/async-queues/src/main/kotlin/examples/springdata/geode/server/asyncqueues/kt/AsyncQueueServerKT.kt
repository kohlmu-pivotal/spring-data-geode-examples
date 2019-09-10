package examples.springdata.geode.server.asyncqueues.kt

import examples.springdata.geode.domain.*
import examples.springdata.geode.server.asyncqueues.kt.config.AsyncQueueServerConfigKT
import examples.springdata.geode.server.asyncqueues.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.server.asyncqueues.kt.repo.OrderProductSummaryRepositoryKT
import examples.springdata.geode.server.asyncqueues.kt.repo.OrderRepositoryKT
import examples.springdata.geode.server.asyncqueues.kt.repo.ProductRepositoryKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import java.math.BigDecimal
import java.util.*

@SpringBootApplication(scanBasePackageClasses = [AsyncQueueServerConfigKT::class])
class AsyncQueueServerKT {

    @Bean
    fun runner(customerRepository: CustomerRepositoryKT, orderRepository: OrderRepositoryKT,
               productRepository: ProductRepositoryKT, orderProductSummaryRepository: OrderProductSummaryRepositoryKT) =
            ApplicationRunner {
                createCustomerData(customerRepository)

                createProducts(productRepository)

                createOrders(productRepository, orderRepository)

                println("Completed creating orders ")

                val allForProductID = orderProductSummaryRepository.findAllForProductID(3L)
                allForProductID.forEach { orderProductSummary -> println("orderProductSummary = $orderProductSummary") }
            }

    private fun createOrders(productRepository: ProductRepositoryKT, orderRepository: OrderRepositoryKT) {
        val random = Random(System.nanoTime())
        val address = Address("it", "doesn't", "matter")
        (1..10L).forEach { orderId ->
            (1..300L).forEach { customerId ->
                val order = Order(orderId, customerId, address)
                (0.. random.nextInt(3) + 2).forEach { _ ->
                    val quantity = random.nextInt(3) + 1
                    val productId = (random.nextInt(3) + 1).toLong()
                    order.add(LineItem(productRepository.findById(productId).get(), quantity))
                }
                orderRepository.save(order)
            }
        }
    }

    private fun createProducts(productRepository: ProductRepositoryKT) {
        productRepository.save(Product(1L, "Apple iPod", BigDecimal("99.99"),
                "An Apple portable music player"))
        productRepository.save(Product(2L, "Apple iPad", BigDecimal("499.99"),
                "An Apple tablet device"))
        val macbook = Product(3L, "Apple macBook", BigDecimal("899.99"),
                "An Apple notebook computer")
        macbook.addAttribute("warranty", "included")
        productRepository.save(macbook)
    }

    private fun createCustomerData(customerRepository: CustomerRepositoryKT) {
        (0..300L).forEach { customerId ->
            customerRepository.save(
                    Customer(customerId,
                            EmailAddress(
                                    "${customerId.toString()}@2.com"),
                            "John$customerId",
                            "Smith$customerId")) }
    }
}

fun main(args: Array<String>) {
    SpringApplicationBuilder(AsyncQueueServerKT::class.java)
        .web(WebApplicationType.NONE)
        .build()
        .run(*args)
}