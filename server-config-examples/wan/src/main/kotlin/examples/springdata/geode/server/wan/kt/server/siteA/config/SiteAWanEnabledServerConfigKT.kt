package examples.springdata.geode.server.wan.kt.server.siteA.config

import examples.springdata.geode.server.wan.kt.config.WanEnabledServerCommonConfigKT
import org.apache.geode.cache.Cache
import org.apache.geode.cache.GemFireCache
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.data.gemfire.config.annotation.CacheServerApplication
import org.springframework.data.gemfire.config.annotation.EnableGemFireProperties
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.wan.GatewayReceiverFactoryBean
import org.springframework.data.gemfire.wan.GatewaySenderFactoryBean

@CacheServerApplication(port = 0, locators = "localhost[10334]",name = "SiteA_Server",enableAutoReconnect = false, logLevel = "error")
@Profile("default", "SiteA")
@EnableLocator(port = 10334)
@EnableGemFireProperties(distributedSystemId = 1, remoteLocators = "localhost[20334]",enableNetworkPartitionDetection = false)
@Import(WanEnabledServerCommonConfigKT::class)
class SiteAWanEnabledServerConfigKT {
    @Bean
    fun createGatewayReceiver(gemFireCache: GemFireCache) =
            GatewayReceiverFactoryBean(gemFireCache as Cache).apply {
                setStartPort(15000)
                setEndPort(15010)
                setManualStart(false)
            }

    @Bean
    @DependsOn("DiskStore")
    fun createGatewaySender(gemFireCache: GemFireCache) =
            GatewaySenderFactoryBean(gemFireCache as Cache).apply {
                setBatchSize(15)
                setBatchTimeInterval(1000)
                setRemoteDistributedSystemId(2)
                isPersistent = false
                setDiskStoreRef("DiskStore")
            }
}