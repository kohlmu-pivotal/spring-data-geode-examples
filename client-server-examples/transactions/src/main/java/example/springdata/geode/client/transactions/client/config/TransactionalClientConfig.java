package example.springdata.geode.client.transactions.client.config;

import example.springdata.geode.client.transactions.client.repo.CustomerRepository;
import example.springdata.geode.client.transactions.client.service.CustomerService;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableIndexing;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.transaction.config.EnableGemfireCacheTransactions;

@ClientCacheApplication
@EnableCachingDefinedRegions(clientRegionShortcut = ClientRegionShortcut.PROXY)
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@ComponentScan(basePackageClasses = CustomerService.class)
@EnableIndexing
@EnableClusterConfiguration(useHttp = true)
@EnableGemfireCacheTransactions
public class TransactionalClientConfig {
}
