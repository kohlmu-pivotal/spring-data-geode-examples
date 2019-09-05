package examples.springdata.geode.server.region.config;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.Order;
import examples.springdata.geode.domain.Product;
import examples.springdata.geode.server.region.repo.CustomerRepository;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.PartitionAttributes;
import org.apache.geode.cache.RegionAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.PartitionAttributesFactoryBean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@Configuration
@PeerCacheApplication
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
public class RegionTypeConfiguration {

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

    @Bean("Orders")
    ReplicatedRegionFactoryBean createOrderRegion(GemFireCache gemFireCache) {
        final ReplicatedRegionFactoryBean<Long, Order> replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean<>();
        replicatedRegionFactoryBean.setCache(gemFireCache);
        replicatedRegionFactoryBean.setRegionName("Orders");
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        return replicatedRegionFactoryBean;
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
    PartitionedRegionFactoryBean createCustomerRegion(GemFireCache gemFireCache, RegionAttributes<Long, Customer> regionAttributes) {
        final PartitionedRegionFactoryBean<Long, Customer> partitionedRegionFactoryBean = new PartitionedRegionFactoryBean<Long, Customer>();
        partitionedRegionFactoryBean.setCache(gemFireCache);
        partitionedRegionFactoryBean.setRegionName("Customers");
        partitionedRegionFactoryBean.setDataPolicy(DataPolicy.PARTITION);
        partitionedRegionFactoryBean.setAttributes(regionAttributes);
        return partitionedRegionFactoryBean;
    }
}