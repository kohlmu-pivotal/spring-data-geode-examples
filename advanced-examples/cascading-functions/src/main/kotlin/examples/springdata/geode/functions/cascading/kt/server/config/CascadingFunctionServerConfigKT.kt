package examples.springdata.geode.functions.cascading.kt.server.config

import examples.springdata.geode.domain.Order
import examples.springdata.geode.domain.Product
import examples.springdata.geode.functions.cascading.kt.server.functions.CascadingFunctionsKT
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.data.gemfire.PartitionedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.CacheServerApplication
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.function.config.EnableGemfireFunctions

@CacheServerApplication(autoStartup = true, copyOnRead = true, port = 0, locators = "localhost[10334]")
@EnableGemfireFunctions
@EnableLocator(host = "localhost", port = 10334)
@Import(CascadingFunctionsKT::class)
class ServerCascadingFunctionServerConfigKT {

    @Bean("Customers")
    protected fun customerRegion(gemfireCache: GemFireCache) =
        PartitionedRegionFactoryBean<Long, Product>().apply {
            cache = gemfireCache
            setRegionName("Customers")
            dataPolicy = DataPolicy.PARTITION
        }

    @Bean("Products")
    protected fun productRegion(gemfireCache: GemFireCache) =
        PartitionedRegionFactoryBean<Long, Product>().apply {
            cache = gemfireCache
            setRegionName("Products")
            dataPolicy = DataPolicy.PARTITION
        }

    @Bean("Orders")
    protected fun orderRegion(gemfireCache: GemFireCache) =
        PartitionedRegionFactoryBean<Long, Order>().apply {
            cache = gemfireCache
            setRegionName("Orders")
            dataPolicy = DataPolicy.PARTITION
        }
}