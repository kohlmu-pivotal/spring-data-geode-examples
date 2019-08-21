package examples.springdata.geode.server.asyncqueues.config;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.Order;
import examples.springdata.geode.domain.Product;
import examples.springdata.geode.server.asyncqueues.listener.OrderAsyncQueueListener;
import examples.springdata.geode.server.asyncqueues.repo.CustomerRepository;
import org.apache.geode.cache.*;
import org.apache.geode.cache.asyncqueue.AsyncEventListener;
import org.apache.geode.cache.asyncqueue.AsyncEventQueue;
import org.springframework.beans.factory.annotation.Qualifier;
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
    AsyncEventListener orderAsyncEventListener(@Qualifier("OrderProductSummary") Region orderProductSummary) {
        return new OrderAsyncQueueListener(orderProductSummary);
    }

    @Bean
    AsyncEventQueueFactoryBean orderAsyncEventQueue(GemFireCache gemFireCache, AsyncEventListener orderAsyncEventListener) {
        final AsyncEventQueueFactoryBean asyncEventQueueFactoryBean = new AsyncEventQueueFactoryBean((Cache) gemFireCache);
        asyncEventQueueFactoryBean.setBatchTimeInterval(1000);
        asyncEventQueueFactoryBean.setBatchSize(5);
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
        partitionAttributesFactoryBean.setRedundantCopies(0);
        return partitionAttributesFactoryBean;
    }

    @Bean(name = "OrderProductSummary")
    PartitionedRegionFactoryBean createOrderProductSummaryRegion(GemFireCache gemFireCache, RegionAttributes regionAttributes) {
        final PartitionedRegionFactoryBean<Long, Order> partitionedRegionFactoryBean = new PartitionedRegionFactoryBean<>();
        partitionedRegionFactoryBean.setCache(gemFireCache);
        partitionedRegionFactoryBean.setRegionName("OrderProductSummary");
        partitionedRegionFactoryBean.setDataPolicy(DataPolicy.PARTITION);
        partitionedRegionFactoryBean.setAttributes(regionAttributes);
        return partitionedRegionFactoryBean;
    }

    @Bean("Orders")
    PartitionedRegionFactoryBean createOrderRegion(GemFireCache gemFireCache, RegionAttributes regionAttributes, AsyncEventQueue orderAsyncEventQueue) {
        final PartitionedRegionFactoryBean<Long, Order> partitionedRegionFactoryBean = new PartitionedRegionFactoryBean<>();
        partitionedRegionFactoryBean.setCache(gemFireCache);
        partitionedRegionFactoryBean.setRegionName("Orders");
        partitionedRegionFactoryBean.setDataPolicy(DataPolicy.PARTITION);
        partitionedRegionFactoryBean.setAttributes(regionAttributes);
        partitionedRegionFactoryBean.setAsyncEventQueues(new AsyncEventQueue[]{orderAsyncEventQueue});
        return partitionedRegionFactoryBean;
    }

    @Bean("Products")
    ReplicatedRegionFactoryBean createProductRegion(GemFireCache gemFireCache) {
        final ReplicatedRegionFactoryBean<Long, Product> replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean<>();
        replicatedRegionFactoryBean.setCache(gemFireCache);
        replicatedRegionFactoryBean.setRegionName("Products");
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        return replicatedRegionFactoryBean;
    }

    @Bean("Customers")
    ReplicatedRegionFactoryBean createCustomerRegion(GemFireCache gemFireCache, RegionAttributes<Long, Customer> regionAttributes) {
        final ReplicatedRegionFactoryBean<Long, Customer> replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean<Long, Customer>();
        replicatedRegionFactoryBean.setCache(gemFireCache);
        replicatedRegionFactoryBean.setRegionName("Customers");
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        return replicatedRegionFactoryBean;
    }
}