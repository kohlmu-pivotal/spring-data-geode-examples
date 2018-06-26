package examples.springdata.geode.function.kt.client

import examples.springdata.geode.domain.*
import examples.springdata.geode.function.kt.client.services.CustomerServiceKT
import examples.springdata.geode.function.kt.client.services.OrderServiceKT
import examples.springdata.geode.function.kt.client.services.ProductServiceKT
import org.springframework.beans.factory.getBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.math.BigDecimal
import java.util.*
import java.util.stream.IntStream
import java.util.stream.LongStream

/**
 * Creates a client to demonstrate server-side function invocations. This example will run queries against that local client data set and
 * again the remote servers. There is no difference in running a function on a client that stores no data locally (PROXY region)
 * or a client that stores data locally (CACHING_PROXY region).
 *
 * @author Udo Kohlmeyer
 */
@SpringBootApplication(scanBasePackageClasses = [FunctionInvocationClientApplicationConfigKT::class])
class FunctionInvocationClientKT(internal val customerServiceKT: CustomerServiceKT,
                                 internal val productServiceKT: ProductServiceKT,
                                 internal val orderServiceKT: OrderServiceKT)

fun main(args: Array<String>) {
    SpringApplication.run(FunctionInvocationClientKT::class.java, *args)?.apply {
        getBean<FunctionInvocationClientKT>(FunctionInvocationClientKT::class).apply {

            createCustomerData(customerServiceKT)

            println("All customers for emailAddresses:3@3.com,2@2.com using function invocation: \n\t " +
                "${customerServiceKT.listAllCustomersForEmailAddress("2@2.com", "3@3.com")}")

            createProducts(productServiceKT)
            println("Running function to sum up all product prices:\n\t $${productServiceKT.sumPricesForAllProducts()[0]}")

            createOrders(productServiceKT, orderServiceKT)
            println("Running function to sum up all order lineItems prices for order 1: \n\t" +
                "$${orderServiceKT.sumPricesForAllProductsForOrder(1)[0]}")
            println("For order: \n\t ${orderServiceKT.findById(1).get()}")
        }
    }
}

internal fun createOrders(productServiceKT: ProductServiceKT, orderServiceKT: OrderServiceKT) {
    val random = Random(System.nanoTime())
    val address = Address("it", "doesn't", "matter")

    LongStream.rangeClosed(1, 100).forEach { orderId ->
        LongStream.rangeClosed(1, 3).forEach { customerId ->
            val order = Order(orderId, customerId, address)
            IntStream.rangeClosed(0, random.nextInt(3) + 1).forEach { _ ->
                val quantity = random.nextInt(3) + 1
                val productId = (random.nextInt(3) + 1).toLong()
                order.add(LineItem(productServiceKT.findById(productId), quantity))
            }
            orderServiceKT.save(order)
        }
    }
}

internal fun createProducts(productServiceKT: ProductServiceKT) {
    productServiceKT.save(Product(1L, "Apple iPod", BigDecimal(99.99), "An Apple portable music player"))
    productServiceKT.save(Product(2L, "Apple iPad", BigDecimal(499.99), "An Apple tablet device"))
    val macbook = Product(3L, "Apple macBook", BigDecimal(899.99), "An Apple notebook computer")
    macbook.addAttribute("warranty", "included")
    productServiceKT.save(macbook)
}

internal fun createCustomerData(customerServiceKT: CustomerServiceKT) {
    println("Inserting 3 entries for keys: 1, 2, 3")
    customerServiceKT.save(Customer(1, EmailAddress("2@2.com"), "John", "Smith"))
    customerServiceKT.save(Customer(2, EmailAddress("3@3.com"), "Frank", "Lamport"))
    customerServiceKT.save(Customer(3, EmailAddress("5@5.com"), "Jude", "Simmons"))
}