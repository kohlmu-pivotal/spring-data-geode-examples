package examples.springdata.geode.server.kt.wan.transport.config

import examples.springdata.geode.server.wan.kt.config.WanEnabledServerCommonConfigKT
import examples.springdata.geode.server.kt.wan.transport.transport.WanTransportEncryptionListenerKT
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(WanEnabledServerCommonConfigKT::class, SiteAWanTransportListenerServerConfigKT::class,
        SiteBWanTransportListenerServerConfigKT::class, WanTransportEncryptionListenerKT::class)
class WanTransportListenerConfigKT
