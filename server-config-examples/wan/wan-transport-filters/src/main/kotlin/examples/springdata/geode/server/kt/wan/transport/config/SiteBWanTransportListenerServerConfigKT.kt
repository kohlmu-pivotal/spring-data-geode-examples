package examples.springdata.geode.server.kt.wan.transport.config

import org.apache.geode.cache.Cache
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.wan.GatewayTransportFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Profile
import org.springframework.data.gemfire.config.annotation.EnableGemFireProperties
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import org.springframework.data.gemfire.wan.GatewayReceiverFactoryBean
import org.springframework.data.gemfire.wan.GatewaySenderFactoryBean

@PeerCacheApplication
@Profile("SiteB")
@EnableLocator(port = 20334)
@EnableGemFireProperties(distributedSystemId = 2, remoteLocators = "localhost[10334]")
internal class SiteBWanTransportListenerServerConfigKT {
    @Bean
    fun createGatewayReceiver(gemFireCache: GemFireCache) =
            GatewayReceiverFactoryBean(gemFireCache as Cache).apply {
                setStartPort(25000)
                setEndPort(25010)
            }

    @Bean
    @DependsOn("DiskStore")
    fun createGatewaySender(gemFireCache: GemFireCache, gatewayTransportFilter: GatewayTransportFilter) =
            GatewaySenderFactoryBean(gemFireCache as Cache).apply {
                setBatchSize(15)
                setBatchTimeInterval(1000)
                setRemoteDistributedSystemId(1)
                setDiskStoreRef("DiskStore")
                setTransportFilters(listOf(gatewayTransportFilter))
                isPersistent = false
            }
}