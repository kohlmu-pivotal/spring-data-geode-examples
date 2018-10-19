package examples.springdata.geode.functions.cascading.client

import examples.springdata.geode.domain.*
import examples.springdata.geode.functions.cascading.client.config.CascadingFunctionClientConfigKT
import examples.springdata.geode.functions.cascading.client.services.CustomerServiceKT
import examples.springdata.geode.functions.cascading.client.services.OrderServiceKT
import examples.springdata.geode.functions.cascading.client.services.ProductServiceKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import java.math.BigDecimal
import java.util.*
import java.util.stream.IntStream

@SpringBootApplication(scanBasePackageClasses = [CascadingFunctionClientConfigKT::class])
class CascadingFunctionClientKT {

    @Bean
    fun runner(customerServiceKT: CustomerServiceKT,
               productServiceKT: ProductServiceKT,
               orderServiceKT: OrderServiceKT): ApplicationRunner = ApplicationRunner {
        createCustomerData(customerServiceKT)

        createProducts(productServiceKT)

        createOrders(productServiceKT, orderServiceKT)

        val listAllCustomers = customerServiceKT.listAllCustomers()
        println("Number of customers retrieved from servers: ${listAllCustomers.size}")

        val findOrdersForCustomer = orderServiceKT.findOrdersForCustomers(listAllCustomers.get(0) as List<Long>)

        print(findOrdersForCustomer)

    }

    private fun createOrders(productServiceKT: ProductServiceKT, orderServiceKT: OrderServiceKT) {
        val random = Random(System.nanoTime())
        val address = Address("it", "doesn't", "matter")

        IntStream.rangeClosed(1, 10).forEach { orderId ->
            IntStream.rangeClosed(1, 10).forEach { customerId ->
                val order = Order(orderId.toLong(), customerId.toLong(), address)
                IntStream.rangeClosed(0, random.nextInt(3) + 1).forEach {
                    val quantity = random.nextInt(3) + 1
                    val productId = (random.nextInt(3) + 1).toLong()
                    order.add(LineItem(productServiceKT.findById(productId), quantity))
                }
                orderServiceKT.save(order)
            }
        }
    }

    private fun createProducts(productServiceKT: ProductServiceKT) {
        productServiceKT.save(Product(1L, "Apple iPod", BigDecimal("99.99"), "An Apple portable music player"))
        productServiceKT.save(Product(2L, "Apple iPad", BigDecimal("499.99"), "An Apple tablet device"))
        Product(3L, "Apple macBook", BigDecimal("899.99"), "An Apple notebook computer").also {
            it.addAttribute("warranty", "included")
            productServiceKT.save(it)
        }

    }

    private fun createCustomerData(customerServiceKT: CustomerServiceKT) {
        IntStream.rangeClosed(1, 10000).parallel().forEach { customerId ->
            customerServiceKT.save(Customer(customerId.toLong(), EmailAddress("2@2.com"), "John+$customerId", "Smith+$customerId"))

        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(CascadingFunctionClientKT::class.java, *args)
}