package example.springdata.geode.client.transactions.kt.client.config

import example.springdata.geode.client.transactions.kt.client.repo.CustomerRepositoryKT
import example.springdata.geode.client.transactions.kt.client.service.CustomerServiceKT
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableClusterDefinedRegions
import org.springframework.data.gemfire.config.annotation.EnableIndexing
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.gemfire.transaction.config.EnableGemfireCacheTransactions
import org.springframework.transaction.annotation.EnableTransactionManagement

@ClientCacheApplication
@EnableClusterDefinedRegions(clientRegionShortcut = ClientRegionShortcut.PROXY)
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@ComponentScan(basePackageClasses = [CustomerServiceKT::class])
@EnableIndexing
@EnableTransactionManagement
@EnableGemfireCacheTransactions
class TransactionalClientConfigKT
