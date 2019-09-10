package examples.springdata.geode.server.wan.kt.config

import examples.springdata.geode.server.wan.kt.server.siteA.config.SiteAWanEnabledServerConfigKT
import examples.springdata.geode.server.wan.kt.server.siteB.config.SiteBWanEnabledServerConfigKT
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(WanEnabledServerCommonConfigKT::class, SiteAWanEnabledServerConfigKT::class, SiteBWanEnabledServerConfigKT::class)
class WanEnableServerConfigKT
