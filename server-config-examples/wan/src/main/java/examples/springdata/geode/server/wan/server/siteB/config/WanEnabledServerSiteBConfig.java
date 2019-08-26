package examples.springdata.geode.server.wan.server.siteB.config;

import com.github.javafaker.Faker;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.server.wan.client.repo.CustomerRepository;
import org.apache.geode.cache.*;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.DiskStoreFactoryBean;
import org.springframework.data.gemfire.PartitionAttributesFactoryBean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;
import org.springframework.data.gemfire.config.annotation.*;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.wan.GatewayReceiverFactoryBean;

import java.io.File;
import java.util.Arrays;

@CacheServerApplication(port = 0, locators = "localhost[10334]")
@EnableLocator(port = 10334)
@EnableManager(start = true, port = 2099)
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@EnableGemFireProperties(distributedSystemId = 2, remoteLocators = "localhost[20334]", enableNetworkPartitionDetection = false, conserveSockets = false)
@EnableLogging
public class WanEnabledServerSiteBConfig {
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
    PartitionAttributesFactoryBean partitionAttributes() {
        final PartitionAttributesFactoryBean<Long, Customer> partitionAttributesFactoryBean = new PartitionAttributesFactoryBean<>();
        partitionAttributesFactoryBean.setTotalNumBuckets(13);
        partitionAttributesFactoryBean.setRedundantCopies(0);
        return partitionAttributesFactoryBean;
    }

    @Bean("Customers")
    PartitionedRegionFactoryBean createCustomerRegion(GemFireCache gemFireCache, RegionAttributes regionAttributes) {
        final PartitionedRegionFactoryBean<Long, Customer> partitionedRegionFactoryBean = new PartitionedRegionFactoryBean<>();
        partitionedRegionFactoryBean.setCache(gemFireCache);
        partitionedRegionFactoryBean.setRegionName("Customers");
        partitionedRegionFactoryBean.setDataPolicy(DataPolicy.PARTITION);
        partitionedRegionFactoryBean.setAttributes(regionAttributes);
        return partitionedRegionFactoryBean;
    }

    @Bean
    GatewayReceiverFactoryBean createGatewayReceiver(GemFireCache gemFireCache) {
        final GatewayReceiverFactoryBean gatewayReceiverFactoryBean = new GatewayReceiverFactoryBean((Cache) gemFireCache);
        gatewayReceiverFactoryBean.setStartPort(25000);
        gatewayReceiverFactoryBean.setEndPort(25010);
        return gatewayReceiverFactoryBean;
    }
}