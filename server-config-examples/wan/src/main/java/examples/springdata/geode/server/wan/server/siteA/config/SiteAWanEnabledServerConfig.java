package examples.springdata.geode.server.wan.server.siteA.config;

import examples.springdata.geode.server.wan.config.WanEnabledServerCommonConfig;
import org.apache.geode.cache.Cache;
import org.apache.geode.cache.GemFireCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.EnableGemFireProperties;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.wan.GatewayReceiverFactoryBean;
import org.springframework.data.gemfire.wan.GatewaySenderFactoryBean;

@CacheServerApplication(port = 0, locators = "localhost[10334]",name = "SiteB_Server",enableAutoReconnect = false)
@Profile({"default", "SiteA"})
@EnableLocator(port = 10334)
@EnableGemFireProperties(distributedSystemId = 1, remoteLocators = "localhost[20334]",enableNetworkPartitionDetection = false)
@Import(WanEnabledServerCommonConfig.class)
public class SiteAWanEnabledServerConfig {
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
