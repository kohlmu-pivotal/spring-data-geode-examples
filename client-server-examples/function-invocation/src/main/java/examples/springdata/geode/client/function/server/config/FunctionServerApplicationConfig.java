package examples.springdata.geode.client.function.server.config;

import examples.springdata.geode.client.function.client.repo.CustomerRepository;
import examples.springdata.geode.client.function.server.functions.CustomerFunctions;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.Order;
import examples.springdata.geode.domain.Product;
import examples.springdata.geode.util.LoggingCacheListener;
import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Scope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.EnableIndexing;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.EnableManager;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctions;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@Configuration
@ComponentScan(basePackageClasses = CustomerFunctions.class)
@EnableGemfireFunctions
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@EnableLocator
@EnableIndexing
@EnableManager
@CacheServerApplication(port = 0)
public class FunctionServerApplicationConfig {

    @Bean
    LoggingCacheListener loggingCacheListener() {
        return new LoggingCacheListener();
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
        return replicatedRegionFactoryBean;
    }

    @Bean
    ReplicatedRegionFactoryBean<Long, Product> createProductRegion(GemFireCache gemfireCache) {
        ReplicatedRegionFactoryBean replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean();
        replicatedRegionFactoryBean.setCache(gemfireCache);
        replicatedRegionFactoryBean.setRegionName("Products");
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        return replicatedRegionFactoryBean;
    }
}
