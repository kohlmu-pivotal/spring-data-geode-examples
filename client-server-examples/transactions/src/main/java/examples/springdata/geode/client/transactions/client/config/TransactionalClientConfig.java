package examples.springdata.geode.client.transactions.client.config;

import examples.springdata.geode.client.transactions.client.repo.CustomerRepository;
import examples.springdata.geode.client.transactions.client.service.CustomerService;
import examples.springdata.geode.domain.Customer;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableClusterDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableIndexing;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.transaction.config.EnableGemfireCacheTransactions;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableClusterDefinedRegions(clientRegionShortcut = ClientRegionShortcut.PROXY)
@EnableIndexing
@EnableTransactionManagement
@EnableGemfireCacheTransactions
@Configuration
@ComponentScan(basePackageClasses = CustomerService.class)
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@ClientCacheApplication(name = "TransactionalClient", logLevel = "error", pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TransactionalClientConfig {

    @Bean("Customers")
    protected ClientRegionFactoryBean<Long, Customer> configureProxyClientCustomerRegion(GemFireCache gemFireCache) {
        ClientRegionFactoryBean<Long,Customer> clientRegionFactoryBean = new ClientRegionFactoryBean<>();
        clientRegionFactoryBean.setCache(gemFireCache);
        clientRegionFactoryBean.setName("Customers");
        clientRegionFactoryBean.setShortcut(ClientRegionShortcut.PROXY);
        return clientRegionFactoryBean;
    }
}