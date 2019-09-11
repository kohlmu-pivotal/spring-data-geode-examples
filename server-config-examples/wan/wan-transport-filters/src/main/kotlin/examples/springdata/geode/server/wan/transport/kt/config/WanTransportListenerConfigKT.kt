package examples.springdata.geode.server.wan.transport.kt.config

import examples.springdata.geode.server.wan.kt.config.WanEnabledServerCommonConfigKT
import examples.springdata.geode.server.wan.kt.server.siteA.config.SiteAWanEnabledServerConfigKT
import examples.springdata.geode.server.wan.transport.kt.transport.WanTransportEncryptionListenerKT
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(WanEnabledServerCommonConfigKT::class, SiteAWanEnabledServerConfigKT::class,
        SiteBWanTransportListenerServerConfigKT::class, WanTransportEncryptionListenerKT::class)
class WanTransportListenerConfigKT
