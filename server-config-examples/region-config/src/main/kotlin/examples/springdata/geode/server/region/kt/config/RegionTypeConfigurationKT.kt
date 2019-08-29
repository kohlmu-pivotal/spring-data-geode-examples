package examples.springdata.geode.server.region.kt.config

import examples.springdata.geode.domain.*
import examples.springdata.geode.server.region.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.server.region.kt.repo.OrderRepositoryKT
import examples.springdata.geode.server.region.kt.repo.ProductRepositoryKT
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.PartitionAttributes
import org.apache.geode.cache.RegionAttributes
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.gemfire.PartitionAttributesFactoryBean
import org.springframework.data.gemfire.PartitionedRegionFactoryBean
import org.springframework.data.gemfire.RegionAttributesFactoryBean
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import java.math.BigDecimal
import java.util.*
import java.util.stream.IntStream
import java.util.stream.LongStream

@Configuration
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
class RegionTypeConfigurationKT {

    @Bean
    @Primary
    internal fun regionAttributes(partitionAttributes: PartitionAttributes<*, *>): RegionAttributesFactoryBean<*, *> {
        val regionAttributesFactoryBean = RegionAttributesFactoryBean<Any, Any>()
        regionAttributesFactoryBean.setPartitionAttributes(partitionAttributes)
        return regionAttributesFactoryBean
    }

    @Bean
    internal fun partitionAttributes(gemFireCache: GemFireCache): PartitionAttributesFactoryBean<*, *> {
        val partitionAttributesFactoryBean = PartitionAttributesFactoryBean<Long, Order>()
        partitionAttributesFactoryBean.setTotalNumBuckets(13)
        partitionAttributesFactoryBean.setRedundantCopies(1)
        return partitionAttributesFactoryBean
    }

    @Bean("Orders")
    internal fun createOrderRegion(gemFireCache: GemFireCache): ReplicatedRegionFactoryBean<Long, Order> {
        val replicatedRegionFactoryBean = ReplicatedRegionFactoryBean<Long, Order>()
        replicatedRegionFactoryBean.cache = gemFireCache
        replicatedRegionFactoryBean.setRegionName("Orders")
        replicatedRegionFactoryBean.dataPolicy = DataPolicy.REPLICATE
        return replicatedRegionFactoryBean
    }

    @Bean("Products")
    internal fun createProductRegion(gemFireCache: GemFireCache): ReplicatedRegionFactoryBean<Long, Product> {
        val replicatedRegionFactoryBean = ReplicatedRegionFactoryBean<Long, Product>()
        replicatedRegionFactoryBean.cache = gemFireCache
        replicatedRegionFactoryBean.setRegionName("Products")
        replicatedRegionFactoryBean.dataPolicy = DataPolicy.REPLICATE
        return replicatedRegionFactoryBean
    }

    @Bean("Customers")
    internal fun createCustomerRegion(gemFireCache: GemFireCache, regionAttributes: RegionAttributes<Long, Customer>): PartitionedRegionFactoryBean<Long, Customer> {
        val partitionedRegionFactoryBean = PartitionedRegionFactoryBean<Long, Customer>()
        partitionedRegionFactoryBean.cache = gemFireCache
        partitionedRegionFactoryBean.setRegionName("Customers")
        partitionedRegionFactoryBean.dataPolicy = DataPolicy.PARTITION
        partitionedRegionFactoryBean.attributes = regionAttributes
        return partitionedRegionFactoryBean
    }

    @Configuration
    @PeerCacheApplication(name = "dataPopulationPeer")
    internal inner class DataPopulationPeerServerKT {
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
}