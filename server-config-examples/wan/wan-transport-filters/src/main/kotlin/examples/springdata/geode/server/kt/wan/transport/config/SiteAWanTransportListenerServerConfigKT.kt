package examples.springdata.geode.server.kt.wan.transport.config

import org.apache.geode.cache.Cache
import org.apache.geode.cache.GemFireCache
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Profile
import org.springframework.data.gemfire.config.annotation.EnableGemFireProperties
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import org.springframework.data.gemfire.wan.GatewayReceiverFactoryBean
import org.springframework.data.gemfire.wan.GatewaySenderFactoryBean

@PeerCacheApplication
@Profile("default", "SiteA")
@EnableLocator(port = 10334)
@EnableGemFireProperties(distributedSystemId = 1, remoteLocators = "localhost[20334]")
class SiteAWanTransportListenerServerConfigKT {
    @Bean
    internal fun createGatewayReceiver(gemFireCache: GemFireCache) =
            GatewayReceiverFactoryBean(gemFireCache as Cache).apply {
                setStartPort(15000)
                setEndPort(15010)
                setManualStart(false)
            }

    @Bean
    @DependsOn("DiskStore")
    internal fun createGatewaySender(gemFireCache: GemFireCache) =
            GatewaySenderFactoryBean(gemFireCache as Cache).apply {
                setBatchSize(15)
                setBatchTimeInterval(1000)
                setRemoteDistributedSystemId(2)
                isPersistent = false
                setDiskStoreRef("DiskStore")
            }
}
