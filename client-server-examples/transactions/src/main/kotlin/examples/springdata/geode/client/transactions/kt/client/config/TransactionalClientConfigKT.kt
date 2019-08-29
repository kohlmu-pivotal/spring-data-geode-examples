package examples.springdata.geode.client.transactions.kt.client.config

import examples.springdata.geode.client.transactions.kt.client.repo.CustomerRepositoryKT
import examples.springdata.geode.client.transactions.kt.client.service.CustomerServiceKT
import examples.springdata.geode.domain.Customer
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableClusterDefinedRegions
import org.springframework.data.gemfire.config.annotation.EnableIndexing
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.gemfire.transaction.config.EnableGemfireCacheTransactions
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableClusterDefinedRegions(clientRegionShortcut = ClientRegionShortcut.PROXY)
@EnableIndexing
@EnableTransactionManagement
@EnableGemfireCacheTransactions
@Configuration
@ComponentScan(basePackageClasses = [CustomerServiceKT::class])
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@ClientCacheApplication(name = "TransactionalClient", logLevel = "error", pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
@EnableAspectJAutoProxy(proxyTargetClass = true)
class TransactionalClientConfigKT {

    @Bean("Customers")
    protected fun configureProxyClientCustomerRegion(gemFireCache: GemFireCache) =
            ClientRegionFactoryBean<Long, Customer>().apply {
                cache = gemFireCache
                setName("Customers")
                setShortcut(ClientRegionShortcut.PROXY)
            }
}