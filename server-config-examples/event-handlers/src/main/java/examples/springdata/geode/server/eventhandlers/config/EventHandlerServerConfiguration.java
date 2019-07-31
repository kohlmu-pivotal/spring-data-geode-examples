package examples.springdata.geode.server.eventhandlers.config;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.Product;
import examples.springdata.geode.server.eventhandlers.repo.CustomerRepository;
import examples.springdata.geode.util.LoggingCacheListener;
import org.apache.geode.cache.*;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import java.util.Collections;

@PeerCacheApplication
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
public class EventHandlerServerConfiguration {

    @Bean
    CacheListener loggingCacheListener() {
        return new LoggingCacheListener();
    }

    @Bean
    CacheWriter customerCacheWriter() {
        return new CustomerCacheWriter();
    }

    @Bean
    CacheLoader productCacheLoader() {
        return new ProductCacheLoader();
    }

    @Bean
    ReplicatedRegionFactoryBean createProductRegion(GemFireCache gemFireCache, CacheListener loggingCacheListener,
                                                    CacheLoader productCacheLoader) {
        final ReplicatedRegionFactoryBean<Long, Product> replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean<>();
        replicatedRegionFactoryBean.setCache(gemFireCache);
        replicatedRegionFactoryBean.setRegionName("Products");
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        replicatedRegionFactoryBean.setCacheListeners(Collections.singletonList(loggingCacheListener).toArray(new CacheListener[]{}));
        replicatedRegionFactoryBean.setCacheLoader(productCacheLoader);
        return replicatedRegionFactoryBean;
    }

    @Bean
    PartitionedRegionFactoryBean createCustomerRegion(GemFireCache gemFireCache,
                                                      CacheWriter customerCacheWriter, CacheListener loggingCacheListener) {
        final PartitionedRegionFactoryBean<Long, Customer> partitionedRegionFactoryBean = new PartitionedRegionFactoryBean<Long, Customer>();
        partitionedRegionFactoryBean.setCache(gemFireCache);
        partitionedRegionFactoryBean.setRegionName("Customers");
        partitionedRegionFactoryBean.setDataPolicy(DataPolicy.PARTITION);
        partitionedRegionFactoryBean.setCacheListeners(Collections.singletonList(loggingCacheListener).toArray(new CacheListener[]{}));
        partitionedRegionFactoryBean.setCacheWriter(customerCacheWriter);
        return partitionedRegionFactoryBean;
    }
}