package example.springdata.geode.server.syncqueues.config;

import example.springdata.geode.server.syncqueues.listener.OrderAsyncQueueListener;
import example.springdata.geode.server.syncqueues.repo.CustomerRepository;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.Order;
import examples.springdata.geode.domain.Product;
import org.apache.geode.cache.*;
import org.apache.geode.cache.asyncqueue.AsyncEventListener;
import org.apache.geode.cache.asyncqueue.AsyncEventQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.PartitionAttributesFactoryBean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.wan.AsyncEventQueueFactoryBean;

@PeerCacheApplication
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
public class AsyncQueueServerConfig {

    @Bean
    AsyncEventListener orderAsyncEventListener() {
        return new OrderAsyncQueueListener();
    }

    @Bean
    AsyncEventQueueFactoryBean orderAsyncEventQueue(GemFireCache gemFireCache, AsyncEventListener orderAsyncEventListener) {
        final AsyncEventQueueFactoryBean asyncEventQueueFactoryBean = new AsyncEventQueueFactoryBean((Cache) gemFireCache);
        asyncEventQueueFactoryBean.setBatchTimeInterval(60000);
        asyncEventQueueFactoryBean.setBatchSize(15);
        asyncEventQueueFactoryBean.setAsyncEventListener(orderAsyncEventListener);
        return asyncEventQueueFactoryBean;
    }

    @Bean
    RegionAttributesFactoryBean regionAttributes(PartitionAttributes partitionAttributes) {
        final RegionAttributesFactoryBean regionAttributesFactoryBean = new RegionAttributesFactoryBean();
        regionAttributesFactoryBean.setPartitionAttributes(partitionAttributes);
        return regionAttributesFactoryBean;
    }

    @Bean
    PartitionAttributesFactoryBean partitionAttributes(GemFireCache gemFireCache) {
        final PartitionAttributesFactoryBean<Long, Order> partitionAttributesFactoryBean = new PartitionAttributesFactoryBean<>();
        partitionAttributesFactoryBean.setTotalNumBuckets(13);
        partitionAttributesFactoryBean.setRedundantCopies(1);
        return partitionAttributesFactoryBean;
    }

    @Bean
    ReplicatedRegionFactoryBean createOrderProductSummaryRegion(GemFireCache gemFireCache, AsyncEventQueue orderAsyncEventQueue) {
        final ReplicatedRegionFactoryBean<Long, Order> replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean<>();
        replicatedRegionFactoryBean.setCache(gemFireCache);
        replicatedRegionFactoryBean.setRegionName("OrderProductSummary");
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.PARTITION);
        replicatedRegionFactoryBean.setAsyncEventQueues(new AsyncEventQueue[]{orderAsyncEventQueue});
        return replicatedRegionFactoryBean;
    }

    @Bean
    PartitionedRegionFactoryBean createOrderRegion(GemFireCache gemFireCache, RegionAttributes regionAttributes, AsyncEventQueue orderAsyncEventQueue) {
        final PartitionedRegionFactoryBean<Long, Order> partitionedRegionFactoryBean = new PartitionedRegionFactoryBean<>();
        partitionedRegionFactoryBean.setCache(gemFireCache);
        partitionedRegionFactoryBean.setRegionName("Orders");
        partitionedRegionFactoryBean.setDataPolicy(DataPolicy.PARTITION);
        partitionedRegionFactoryBean.setAttributes(regionAttributes);
        partitionedRegionFactoryBean.setAsyncEventQueues(new AsyncEventQueue[]{orderAsyncEventQueue});
        return partitionedRegionFactoryBean;
    }

    @Bean
    ReplicatedRegionFactoryBean createProductRegion(GemFireCache gemFireCache) {
        final ReplicatedRegionFactoryBean<Long, Product> replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean<>();
        replicatedRegionFactoryBean.setCache(gemFireCache);
        replicatedRegionFactoryBean.setRegionName("Products");
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        return replicatedRegionFactoryBean;
    }

    @Bean
    ReplicatedRegionFactoryBean createCustomerRegion(GemFireCache gemFireCache, RegionAttributes<Long, Customer> regionAttributes) {
        final ReplicatedRegionFactoryBean<Long, Customer> replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean<Long, Customer>();
        replicatedRegionFactoryBean.setCache(gemFireCache);
        replicatedRegionFactoryBean.setRegionName("Customers");
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        return replicatedRegionFactoryBean;
    }

}
