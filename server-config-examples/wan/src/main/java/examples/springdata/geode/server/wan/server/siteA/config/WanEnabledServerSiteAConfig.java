package examples.springdata.geode.server.wan.server.siteA.config;

import com.github.javafaker.Faker;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.server.wan.client.repo.CustomerRepository;
import org.apache.geode.cache.*;
import org.apache.geode.cache.wan.GatewaySender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.gemfire.DiskStoreFactoryBean;
import org.springframework.data.gemfire.PartitionAttributesFactoryBean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;
import org.springframework.data.gemfire.config.annotation.*;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.wan.GatewaySenderFactoryBean;

import java.io.File;
import java.util.Arrays;

@Configuration
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@CacheServerApplication(port = 0, locators = "localhost[20334]", logLevel = "error")
@EnableLocator(port = 20334)
@EnableManager(start = true, port = 1099)
@EnableGemFireProperties(distributedSystemId = 1, remoteLocators = "localhost[10334]", enableNetworkPartitionDetection = false, conserveSockets = false)
public class WanEnabledServerSiteAConfig {
    private Faker faker = new Faker();

    @Bean(name = "DiskStore")
    DiskStoreFactoryBean diskStore(GemFireCache gemFireCache) {
        final DiskStoreFactoryBean diskStoreFactoryBean = new DiskStoreFactoryBean();
        new File("/tmp/" + faker.name().firstName()).mkdirs();
        final DiskStoreFactoryBean.DiskDir[] diskDirs = {new DiskStoreFactoryBean.DiskDir("/tmp/" + faker.name().firstName())};
        diskStoreFactoryBean.setDiskDirs(Arrays.asList(diskDirs));
        diskStoreFactoryBean.setCache(gemFireCache);
        return diskStoreFactoryBean;
    }

    @Bean
    RegionAttributesFactoryBean regionAttributes(PartitionAttributes partitionAttributes) {
        final RegionAttributesFactoryBean regionAttributesFactoryBean = new RegionAttributesFactoryBean();
        regionAttributesFactoryBean.setPartitionAttributes(partitionAttributes);
        return regionAttributesFactoryBean;
    }

    @Bean
    PartitionAttributesFactoryBean partitionAttributes(GemFireCache gemFireCache) {
        final PartitionAttributesFactoryBean<Long, Customer> partitionAttributesFactoryBean = new PartitionAttributesFactoryBean<>();
        partitionAttributesFactoryBean.setTotalNumBuckets(13);
        partitionAttributesFactoryBean.setRedundantCopies(0);
        return partitionAttributesFactoryBean;
    }

    @Bean("Customers")
    PartitionedRegionFactoryBean createCustomerRegion(GemFireCache gemFireCache, RegionAttributes regionAttributes, GatewaySender gatewaySender) {
        final PartitionedRegionFactoryBean<Long, Customer> partitionedRegionFactoryBean = new PartitionedRegionFactoryBean<>();
        partitionedRegionFactoryBean.setCache(gemFireCache);
        partitionedRegionFactoryBean.setRegionName("Customers");
        partitionedRegionFactoryBean.setDataPolicy(DataPolicy.PARTITION);
        partitionedRegionFactoryBean.setAttributes(regionAttributes);
        partitionedRegionFactoryBean.setGatewaySenders(new GatewaySender[]{gatewaySender});
        return partitionedRegionFactoryBean;
    }

    @Bean
    @DependsOn("DiskStore")
    GatewaySenderFactoryBean createGatewaySender(GemFireCache gemFireCache) {
        final GatewaySenderFactoryBean gatewaySenderFactoryBean = new GatewaySenderFactoryBean((Cache) gemFireCache);
        gatewaySenderFactoryBean.setBatchSize(15);
        gatewaySenderFactoryBean.setBatchTimeInterval(1000);
        gatewaySenderFactoryBean.setRemoteDistributedSystemId(2);
        gatewaySenderFactoryBean.setPersistent(false);
        gatewaySenderFactoryBean.setDiskStoreRef("DiskStore");
        return gatewaySenderFactoryBean;
    }
}