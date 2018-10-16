package examples.springdata.geode.server.wan.kt.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(WanEnabledServerCommonConfigKT::class, SiteAWanEnabledServerConfigKT::class, SiteBWanEnabledServerConfigKT::class)
class WanEnableServerConfigKT
