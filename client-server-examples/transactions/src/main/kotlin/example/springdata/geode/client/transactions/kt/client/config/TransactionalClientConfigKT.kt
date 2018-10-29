package example.springdata.geode.client.transactions.kt.client.config

import example.springdata.geode.client.transactions.kt.client.repo.CustomerRepositoryKT
import example.springdata.geode.client.transactions.kt.client.service.CustomerServiceKT
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.Order
import examples.springdata.geode.domain.Product
import examples.springdata.geode.util.CustomerTransactionListener
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.TransactionListener
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer
import org.springframework.data.gemfire.config.annotation.EnableIndexing
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.gemfire.transaction.config.EnableGemfireCacheTransactions
import org.springframework.transaction.annotation.EnableTransactionManagement

@ClientCacheApplication(locators = [ClientCacheApplication.Locator(host = "localhost", port = 10334)])
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@ComponentScan(basePackageClasses = [CustomerServiceKT::class])
@EnableIndexing
@EnableTransactionManagement
@EnableGemfireCacheTransactions
class TransactionalClientConfigKT {

    @Bean
    internal fun customerTransactionListener() = CustomerTransactionListener()

    @Bean
    fun transactionListenerRegistrationConfigurer(customerTransactionListener: TransactionListener): ClientCacheConfigurer =
            ClientCacheConfigurer { _, clientCacheFactoryBean ->
                clientCacheFactoryBean.transactionListeners = listOf(customerTransactionListener)
            }

    @Bean("Customers")
    protected fun configureProxyClientCustomerRegion(gemFireCache: GemFireCache) =
            ClientRegionFactoryBean<Long, Customer>().apply {
                cache = gemFireCache
                setName("Customers")
                setShortcut(ClientRegionShortcut.PROXY)
            }

    @Bean("Orders")
    protected fun configureProxyClientOrderRegion(gemFireCache: GemFireCache) =
            ClientRegionFactoryBean<Long, Order>().apply {
                cache = gemFireCache
                setName("Orders")
                setShortcut(ClientRegionShortcut.PROXY)
            }

    @Bean("Products")
    protected fun configureProxyClientProductRegion(gemFireCache: GemFireCache) =
            ClientRegionFactoryBean<Long, Product>().apply {
                cache = gemFireCache
                setName("Products")
                setShortcut(ClientRegionShortcut.PROXY)
            }
}
