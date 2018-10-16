package examples.springdata.geode.util

import com.github.javafaker.Faker
import examples.springdata.geode.domain.*
import org.springframework.data.repository.CrudRepository
import java.math.BigDecimal
import java.util.*

fun createProducts(numberOfProducts: Int, repository: CrudRepository<Product, Long>) {
    val faker = Faker()
    val fakerCommerce = faker.commerce()
    (0..numberOfProducts).forEachIndexed { index, _ ->
        repository.save(
                Product(index.toLong(), fakerCommerce.productName(), BigDecimal(fakerCommerce.price(0.01, 100000.0))))
    }
}

fun createCustomers(numberOfCustomer: Int, repository: CrudRepository<Customer, Long>) {
    val faker = Faker()
    val fakerName = faker.name()
    val fakerInternet = faker.internet()
    (0..numberOfCustomer).forEachIndexed { index, _ ->
        repository.save(Customer(index.toLong(),
                EmailAddress(fakerInternet.emailAddress()), fakerName.firstName(), fakerName.lastName()))
    }
}

fun createOrders(numberOfOrders: Int, numberOfCustomer: Int, numberOfProducts: Int = 300, maxItemsPerOrder: Int, orderRepository: CrudRepository<Order, Long>,
                 productRepository: CrudRepository<Product, Long>) {
    val random = Random(System.nanoTime())

    val faker = Faker()
    val fakerAddress = faker.address()
    val address = Address(fakerAddress.streetAddress(), fakerAddress.city(), fakerAddress.country())

    (0L..numberOfCustomer.toLong()).forEach { customerId ->
        (0L..numberOfOrders.toLong()).forEach { orderId ->
            val order = Order(orderId, customerId, address)
            (0..random.nextInt(maxItemsPerOrder)).forEach {
                val quantity = random.nextInt(10) + 1
                val productId = (random.nextInt(numberOfProducts)).toLong()
                order.add(LineItem(productRepository.findById(productId).get(), quantity))
            }
            orderRepository.save(order)
        }
    }
}