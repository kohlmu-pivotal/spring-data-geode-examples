package org.springframework.data.examples.geode.function.kt.client

import org.springframework.beans.factory.getBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.examples.geode.function.kt.client.services.CustomerServiceKT
import org.springframework.data.examples.geode.function.kt.client.services.OrderServiceKT
import org.springframework.data.examples.geode.function.kt.client.services.ProductServiceKT
import org.springframework.data.examples.geode.model.*
import java.math.BigDecimal
import java.util.*

/**
 * Creates a client to demonstrate OQL queries. This example will run queries against that local client data set and
 * again the remote servers. Depending on profile selected, the local query will either not return results (profile=proxy)
 * or it will return the same results as the remote query (profile=localCache)
 *
 * @author Udo Kohlmeyer
 */
@SpringBootApplication(scanBasePackageClasses = [FunctionInvocationClientKT::class])
class FunctionInvocationClientKT(internal val customerServiceKT: CustomerServiceKT,
                                 internal val productServiceKT: ProductServiceKT,
                                 internal val orderServiceKT: OrderServiceKT)

fun main(args: Array<String>) {
    SpringApplication.run(FunctionInvocationClientKT::class.java, *args)?.apply {
        getBean<FunctionInvocationClientKT>(FunctionInvocationClientKT::class).apply {

            createCustomerData()

            println("All customers for emailAddresses:3@3.com,2@2.com using function invocation: \n\t ${customerServiceKT.listAllCustomersForEmailAddress("2@2.com", "3@3.com")}")

            createProducts()
            println("${productServiceKT.sumPricesForAllProducts()}")

            createOrders()
            println("${orderServiceKT.sumPricesForAllProductsForOrder(1)}")
        }
    }
}

private fun FunctionInvocationClientKT.createOrders() {
    val random = Random(Date().time)
    val address = Address("it", "doesn't", "matter")
    for (orderId in 1..100L) {
        for (customerId in 1..3L) {
            val order = Order(orderId, customerId, address)
            for (i in 0 until random.nextInt(3) + 1) {
                val quantity = random.nextInt(3) + 1
                val productId = (random.nextInt(3) + 1).toLong()
                order.add(LineItem(productServiceKT.findById(productId), quantity))
            }
            orderServiceKT.save(order)
        }
    }
}

private fun FunctionInvocationClientKT.createProducts() {
    productServiceKT.save(Product(1L, "Apple iPod", BigDecimal(99.99), "An Apple portable music player"))
    productServiceKT.save(Product(2L, "Apple iPad", BigDecimal(499.99), "An Apple tablet device"))
    val macbook = Product(3L, "Apple macBook", BigDecimal(899.99), "An Apple notebook computer")
    macbook.addAttribute("warranty", "included")
    productServiceKT.save(macbook)
}

private fun FunctionInvocationClientKT.createCustomerData() {
    println("Inserting 3 entries for keys: 1, 2, 3")
    customerServiceKT.save(Customer(1, EmailAddress("2@2.com"), "John", "Smith"))
    customerServiceKT.save(Customer(2, EmailAddress("3@3.com"), "Frank", "Lamport"))
    customerServiceKT.save(Customer(3, EmailAddress("5@5.com"), "Jude", "Simmons"))
}