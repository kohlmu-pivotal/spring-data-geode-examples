package example.springdata.geode.server.kt.region.config

import example.springdata.geode.server.kt.region.repo.CustomerRepositoryKT
import example.springdata.geode.server.kt.region.repo.OrderRepositoryKT
import example.springdata.geode.server.kt.region.repo.ProductRepositoryKT
import examples.springdata.geode.domain.*
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.PartitionAttributes
import org.apache.geode.cache.RegionAttributes
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.gemfire.PartitionAttributesFactoryBean
import org.springframework.data.gemfire.PartitionedRegionFactoryBean
import org.springframework.data.gemfire.RegionAttributesFactoryBean
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import java.math.BigDecimal
import java.util.stream.IntStream
import java.util.stream.LongStream
import kotlin.random.Random


@Configuration
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
class RegionTypeConfigurationKT {

    @Bean
    fun createCustomerRegion(gemfireCache: GemFireCache, regionAttributes: RegionAttributes<Long, Customer>) =
            PartitionedRegionFactoryBean<Long, Customer>().apply {
                cache = gemfireCache
                setRegionName("Customers")
                dataPolicy = DataPolicy.PARTITION
                attributes = regionAttributes
            }

    @Bean
    fun regionAttributes(partitionAttributes: PartitionAttributes<Any, Any>) =
            RegionAttributesFactoryBean().apply { setPartitionAttributes(partitionAttributes) }

    @Bean
    fun partitionAttributes(gemfireCache: GemFireCache) =
            PartitionAttributesFactoryBean<Long, Order>().apply {
                setTotalNumBuckets(13)
                setRedundantCopies(1)
            }

    @Bean
    fun createOrderRegion(gemfireCache: GemFireCache) =
            ReplicatedRegionFactoryBean<Long, Order>().apply {
                cache = gemfireCache
                setRegionName("Orders")
                dataPolicy = DataPolicy.REPLICATE
            }

    @Bean
    fun createProductRegion(gemfireCache: GemFireCache) =
            ReplicatedRegionFactoryBean<Long, Product>().apply {
                cache = gemfireCache
                setRegionName("Products")
                dataPolicy = DataPolicy.REPLICATE
            }

    @Profile("default", "embeddedLocator")
    @Configuration
    @EnableLocator
    @PeerCacheApplication(name = "embeddedLocator")
    class EmbeddedLocatorKT

    @Profile("peer")
    @Configuration
    @PeerCacheApplication(locators = "localhost[10334]", name = "dataPeer")
    class PeerServerKT

    @Profile("dataPopulationPeer")
    @Configuration
    @PeerCacheApplication(locators = "localhost[10334]", name = "dataPopulator")
    class DataPopulationPeerServerKT {
        @Bean
        fun runner(customerRepositoryKT: CustomerRepositoryKT,
                   orderRepositoryKT: OrderRepositoryKT,
                   productRepositoryKT: ProductRepositoryKT) = ApplicationRunner {
            createCustomerData(customerRepositoryKT)

            createProducts(productRepositoryKT)

            createOrders(productRepositoryKT, orderRepositoryKT)
            println("For order: \n\t ${orderRepositoryKT.findById(1).get()}")
        }

        private fun createOrders(productRepositoryKT: ProductRepositoryKT, orderRepositoryKT: OrderRepositoryKT) {
            val random = Random(System.nanoTime())
            val address = Address("it", "doesn't", "matter")

            LongStream.rangeClosed(1, 100).forEach { orderId ->
                LongStream.rangeClosed(1, 3000).forEach { customerId ->
                    val order = Order(orderId, customerId, address)
                    IntStream.rangeClosed(0, random.nextInt(3) + 1).forEach {
                        val quantity = random.nextInt(3) + 1
                        val productId = (random.nextInt(3) + 1).toLong()
                        order.add(LineItem(productRepositoryKT.findById(productId).get(), quantity))
                    }
                    orderRepositoryKT.save(order)
                }
            }
        }

        private fun createProducts(productRepositoryKT: ProductRepositoryKT) {
            productRepositoryKT.save(Product(1L, "Apple iPod", BigDecimal("99.99"), "An Apple portable music player"))
            productRepositoryKT.save(Product(2L, "Apple iPad", BigDecimal("499.99"), "An Apple tablet device"))
            Product(3L, "Apple macBook", BigDecimal("899.99"), "An Apple notebook computer").also {
                it.addAttribute("warranty", "included")
                productRepositoryKT.save(it)
            }

        }

        private fun createCustomerData(customerRepositoryKT: CustomerRepositoryKT) {
            println("Inserting 3000 customers")
            for (id: Long in 0L..3000L) {
                customerRepositoryKT.save(Customer(id, EmailAddress("$id@$id.com"), "John$id", "Smith$id"))
            }
        }
    }
}