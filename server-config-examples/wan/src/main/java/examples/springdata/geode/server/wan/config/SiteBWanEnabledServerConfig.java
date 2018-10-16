package examples.springdata.geode.server.wan.config;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.GemFireCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.config.annotation.EnableGemFireProperties;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.gemfire.wan.GatewayReceiverFactoryBean;
import org.springframework.data.gemfire.wan.GatewaySenderFactoryBean;

@PeerCacheApplication
@Profile("SiteB")
@EnableLocator(port = 20334)
@EnableGemFireProperties(distributedSystemId = 2, remoteLocators = "localhost[10334]")
class SiteBWanEnabledServerConfig {
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