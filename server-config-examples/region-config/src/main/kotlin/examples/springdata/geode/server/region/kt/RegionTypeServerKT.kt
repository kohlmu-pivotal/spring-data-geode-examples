package examples.springdata.geode.server.region.kt

import examples.springdata.geode.domain.*
import examples.springdata.geode.server.region.kt.config.RegionTypeConfigurationKT
import examples.springdata.geode.server.region.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.server.region.kt.repo.OrderRepositoryKT
import examples.springdata.geode.server.region.kt.repo.ProductRepositoryKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import java.math.BigDecimal
import java.util.*
import java.util.stream.IntStream
import java.util.stream.LongStream

@SpringBootApplication(scanBasePackageClasses = [RegionTypeConfigurationKT::class])
class RegionTypeServerKT {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(RegionTypeServerKT::class.java)
                    .web(WebApplicationType.NONE)
                    .build()
                    .run(*args)
        }
    }

    @Bean
    fun runner(customerRepository: CustomerRepositoryKT, orderRepository: OrderRepositoryKT,
               productRepository: ProductRepositoryKT): ApplicationRunner {
        return ApplicationRunner {
            createCustomerData(customerRepository)

            createProducts(productRepository)

            createOrders(productRepository, orderRepository)

            println("There are " + customerRepository.count() + " customers")
            println("There are " + productRepository.count() + " products")
            println("There are " + orderRepository.count() + " orders")
        }
    }

    private fun createOrders(productRepository: ProductRepositoryKT, orderRepository: OrderRepositoryKT) {
        val random = Random(System.nanoTime())
        val address = Address("it", "doesn't", "matter")
        LongStream.rangeClosed(1, 100).forEach { orderId ->
            LongStream.rangeClosed(1, 3000).forEach { customerId ->
                val order = Order(orderId, customerId, address)
                IntStream.rangeClosed(0, random.nextInt(3) + 1).forEach { lineItemCount ->
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
        println("Inserting 3 entries for keys: 1, 2, 3")
        LongStream.rangeClosed(0, 3000)
                .parallel()
                .forEach { customerId ->
                    customerRepository.save(Customer(customerId, EmailAddress("$customerId@2.com"),
                            "John$customerId", "Smith$customerId"))
                }
    }
}