package examples.springdata.geode.functions.cascading.client.config

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.Order
import examples.springdata.geode.domain.Product
import examples.springdata.geode.functions.cascading.client.functions.CustomerFunctionExecutionsKT
import examples.springdata.geode.functions.cascading.client.repo.CustomerRepositoryKT
import examples.springdata.geode.functions.cascading.client.services.CustomerServiceKT
import examples.springdata.geode.functions.cascading.client.services.OrderServiceKT
import examples.springdata.geode.functions.cascading.client.services.ProductServiceKT
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer
import org.springframework.data.gemfire.function.config.EnableGemfireFunctionExecutions
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.gemfire.support.ConnectionEndpoint

@ClientCacheApplication(name = "CascadingFunctionClientCache", logLevel = "error", pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
@EnableGemfireFunctionExecutions(basePackageClasses = [CustomerFunctionExecutionsKT::class])
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
//@Import(ProductServiceKT::class, OrderServiceKT::class, CustomerFunctionExecutionsKT::class)
@ComponentScan(basePackageClasses = [CustomerServiceKT::class])
class CascadingFunctionClientConfigKT {
//
//    @Bean
//    protected fun customerService(customerRepositoryKT: CustomerRepositoryKT, customerFunctionExecutionsKT: CustomerFunctionExecutionsKT) = CustomerServiceKT(customerRepositoryKT, customerFunctionExecutionsKT)

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

    @Bean
    internal fun clientCacheServerConfigurer(
        @Value("\${spring.data.geode.locator.host:localhost}") hostname: String,
        @Value("\${spring.data.geode.locator.port:10334}") port: Int) =
        ClientCacheConfigurer { _, clientCacheFactoryBean ->
            clientCacheFactoryBean.setLocators(listOf(ConnectionEndpoint(hostname, port)))
        }
}