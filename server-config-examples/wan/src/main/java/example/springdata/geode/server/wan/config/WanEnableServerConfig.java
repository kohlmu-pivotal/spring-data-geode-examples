package example.springdata.geode.server.wan.config;

import com.github.javafaker.Faker;
import example.springdata.geode.server.wan.repo.CustomerRepository;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.util.LoggingCacheListener;
import org.apache.geode.cache.*;
import org.apache.geode.cache.wan.GatewaySender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.DiskStoreFactoryBean;
import org.springframework.data.gemfire.PartitionAttributesFactoryBean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableGemFireProperties;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.wan.GatewayReceiverFactoryBean;
import org.springframework.data.gemfire.wan.GatewaySenderFactoryBean;

import java.io.File;
import java.util.Arrays;

@Configuration
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
public class WanEnableServerConfig {
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
    CacheListener customerCacheListener() {
        return new LoggingCacheListener();
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

    @Bean
    PartitionedRegionFactoryBean createCustomerRegion(GemFireCache gemFireCache, RegionAttributes regionAttributes, GatewaySender gatewaySender, CacheListener customerCacheListener) {
        final PartitionedRegionFactoryBean<Long, Customer> partitionedRegionFactoryBean = new PartitionedRegionFactoryBean<>();
        partitionedRegionFactoryBean.setCache(gemFireCache);
        partitionedRegionFactoryBean.setRegionName("Customers");
        partitionedRegionFactoryBean.setDataPolicy(DataPolicy.PARTITION);
        partitionedRegionFactoryBean.setAttributes(regionAttributes);
        partitionedRegionFactoryBean.setGatewaySenders(new GatewaySender[]{gatewaySender});
        partitionedRegionFactoryBean.setCacheListeners(new CacheListener[]{customerCacheListener});
        return partitionedRegionFactoryBean;
    }

    @PeerCacheApplication
    @Profile({"default", "SiteA"})
    @EnableLocator(port = 10334)
    @EnableGemFireProperties(distributedSystemId = 1, remoteLocators = "localhost[20334]")
    class SiteAWanEnabledServer {
        @Bean
        GatewayReceiverFactoryBean createGatewayReceiver(GemFireCache gemFireCache) {
            final GatewayReceiverFactoryBean gatewayReceiverFactoryBean = new GatewayReceiverFactoryBean((Cache) gemFireCache);
            gatewayReceiverFactoryBean.setStartPort(15000);
            gatewayReceiverFactoryBean.setEndPort(15010);
            gatewayReceiverFactoryBean.setManualStart(false);
            return gatewayReceiverFactoryBean;
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

    @PeerCacheApplication
    @Profile("SiteB")
    @EnableLocator(port = 20334)
    @EnableGemFireProperties(distributedSystemId = 2, remoteLocators = "localhost[10334]")
    class SiteBWanEnabledServer {
        @Bean
        GatewayReceiverFactoryBean createGatewayReceiver(GemFireCache gemFireCache) {
            final GatewayReceiverFactoryBean gatewayReceiverFactoryBean = new GatewayReceiverFactoryBean((Cache) gemFireCache);
            gatewayReceiverFactoryBean.setStartPort(25000);
            gatewayReceiverFactoryBean.setEndPort(25010);
            return gatewayReceiverFactoryBean;
        }

        @Bean
        @DependsOn("DiskStore")
        GatewaySenderFactoryBean createGatewaySender(GemFireCache gemFireCache) {
            final GatewaySenderFactoryBean gatewaySenderFactoryBean = new GatewaySenderFactoryBean((Cache) gemFireCache);
            gatewaySenderFactoryBean.setBatchSize(15);
            gatewaySenderFactoryBean.setBatchTimeInterval(1000);
            gatewaySenderFactoryBean.setRemoteDistributedSystemId(1);
            gatewaySenderFactoryBean.setDiskStoreRef("DiskStore");
            return gatewaySenderFactoryBean;
        }
    }
}
