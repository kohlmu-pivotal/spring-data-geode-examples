package examples.springdata.geode.function.kt.server

import examples.springdata.geode.common.kt.server.ServerApplicationConfigKT
import examples.springdata.geode.domain.Order
import examples.springdata.geode.domain.Product
import examples.springdata.geode.function.kt.server.repo.CustomerRepositoryKT
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.function.config.EnableGemfireFunctions
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@Configuration
@EnableGemfireFunctions
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@Import(ServerApplicationConfigKT::class)
class FunctionServerApplicationConfigKT {

    @Bean("Products")
    protected fun productRegion(gemfireCache: GemFireCache) =
        ReplicatedRegionFactoryBean<Long, Product>().apply {
            cache = gemfireCache
            setRegionName("Products")
            dataPolicy = DataPolicy.REPLICATE
        }

    @Bean("Orders")
    protected fun orderRegion(gemfireCache: GemFireCache) =
        ReplicatedRegionFactoryBean<Long, Order>().apply {
            cache = gemfireCache
            setRegionName("Orders")
            dataPolicy = DataPolicy.REPLICATE
        }
}