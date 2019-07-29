package examples.springdata.geode.functions.cascading.kt.client.config

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.Order
import examples.springdata.geode.domain.Product
import examples.springdata.geode.functions.cascading.kt.client.functions.CustomerFunctionExecutionsKT
import examples.springdata.geode.functions.cascading.kt.client.repo.CustomerRepositoryKT
import examples.springdata.geode.functions.cascading.kt.client.services.CustomerServiceKT
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.function.config.EnableGemfireFunctionExecutions
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.gemfire.transaction.config.EnableGemfireCacheTransactions
import org.springframework.transaction.annotation.EnableTransactionManagement

@ClientCacheApplication(name = "CascadingFunctionClientCache", logLevel = "error", pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
@EnableGemfireFunctionExecutions(basePackageClasses = [CustomerFunctionExecutionsKT::class])
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@ComponentScan(basePackageClasses = [CustomerServiceKT::class])
@EnableGemfireCacheTransactions
@EnableTransactionManagement
class CascadingFunctionClientConfigKT {

    @Bean("Customers")
    protected fun configureProxyClientCustomerRegion(gemFireCache: GemFireCache) =
        ClientRegionFactoryBean<Long, Customer>().apply {
            cache = gemFireCache
            setName("Customers")
            setShortcut(ClientRegionShortcut.PROXY)
        }

    @Bean("Products")
    protected fun configureProxyClientProductRegion(gemFireCache: GemFireCache) =
        ClientRegionFactoryBean<Long, Product>().apply {
            cache = gemFireCache
            setName("Products")
            setShortcut(ClientRegionShortcut.PROXY)
        }

    @Bean("Orders")
    protected fun configureProxyClientOrderRegion(gemFireCache: GemFireCache) =
        ClientRegionFactoryBean<Long, Order>().apply {
            cache = gemFireCache
            setName("Orders")
            setShortcut(ClientRegionShortcut.PROXY)
        }
}