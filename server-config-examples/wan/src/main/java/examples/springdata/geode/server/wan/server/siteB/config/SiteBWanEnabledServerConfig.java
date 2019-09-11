package examples.springdata.geode.server.wan.server.siteB.config;

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

@CacheServerApplication(port = 0, locators = "localhost[20334]",name = "SiteB_Server",enableAutoReconnect = false, logLevel = "error")
@Profile("SiteB")
@EnableLocator(port = 20334)
@EnableGemFireProperties(distributedSystemId = 2, remoteLocators = "localhost[10334]",enableNetworkPartitionDetection = false)
@Import(WanEnabledServerCommonConfig.class)
public class SiteBWanEnabledServerConfig {
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