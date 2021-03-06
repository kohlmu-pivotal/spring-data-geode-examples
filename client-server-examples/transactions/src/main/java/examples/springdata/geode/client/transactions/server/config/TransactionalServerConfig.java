package examples.springdata.geode.client.transactions.server.config;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.Order;
import examples.springdata.geode.domain.Product;
import examples.springdata.geode.util.CustomerTransactionListener;
import examples.springdata.geode.util.CustomerTransactionWriter;
import examples.springdata.geode.util.LoggingCacheListener;
import org.apache.geode.cache.*;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.*;
import org.springframework.data.gemfire.transaction.config.EnableGemfireCacheTransactions;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Collections;

@EnableLocator
@EnableTransactionManagement
@EnableGemfireCacheTransactions
@EnableClusterConfiguration(useHttp = true)
@EnableManager(start = true)
@EnableHttpService
@CacheServerApplication
public class TransactionalServerConfig {

    @Bean
    LoggingCacheListener loggingCacheListener() {
        return new LoggingCacheListener();
    }

    @Bean
    TransactionWriter customerTransactionWriter() {
        return new CustomerTransactionWriter();
    }

    @Bean
    TransactionListener customerTransactionListener() {
        return new CustomerTransactionListener();
    }

    @Bean
    PeerCacheConfigurer transactionListenerRegistrationConfigurer(TransactionWriter customerTransactionWriter, TransactionListener customerTransactionListener) {
        return (beanName, peerCacheFactoryBean) ->
        {
            peerCacheFactoryBean.setTransactionListeners(Collections.singletonList(customerTransactionListener));
            peerCacheFactoryBean.setTransactionWriter(customerTransactionWriter);
        };
    }

    @Bean
    ReplicatedRegionFactoryBean<Long, Customer> createCustomerRegion(GemFireCache gemfireCache) {
        ReplicatedRegionFactoryBean replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean();
        replicatedRegionFactoryBean.setCache(gemfireCache);
        replicatedRegionFactoryBean.setRegionName("Customers");
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        replicatedRegionFactoryBean.setScope(Scope.DISTRIBUTED_ACK);
        replicatedRegionFactoryBean.setCacheListeners(new CacheListener[]{loggingCacheListener()});
        return replicatedRegionFactoryBean;
    }

    @Bean
    ReplicatedRegionFactoryBean<Long, Order> createOrderRegion(GemFireCache gemfireCache) {
        ReplicatedRegionFactoryBean replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean();
        replicatedRegionFactoryBean.setCache(gemfireCache);
        replicatedRegionFactoryBean.setRegionName("Orders");
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        replicatedRegionFactoryBean.setScope(Scope.DISTRIBUTED_ACK);
        return replicatedRegionFactoryBean;
    }

    @Bean
    ReplicatedRegionFactoryBean<Long, Product> createProductRegion(GemFireCache gemfireCache) {
        ReplicatedRegionFactoryBean replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean();
        replicatedRegionFactoryBean.setCache(gemfireCache);
        replicatedRegionFactoryBean.setRegionName("Products");
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        replicatedRegionFactoryBean.setScope(Scope.DISTRIBUTED_ACK);
        return replicatedRegionFactoryBean;
    }
}
