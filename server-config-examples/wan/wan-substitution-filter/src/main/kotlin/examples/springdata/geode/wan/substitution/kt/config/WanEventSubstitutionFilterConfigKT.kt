package examples.springdata.geode.wan.substitution.kt.config


import examples.springdata.geode.server.wan.kt.config.WanEnabledServerCommonConfigKT
import examples.springdata.geode.server.wan.kt.server.siteA.config.SiteAWanEnabledServerConfigKT
import examples.springdata.geode.wan.substitution.kt.filter.WanEventSubstitutionFilterKT
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(WanEnabledServerCommonConfigKT::class, SiteAWanEnabledServerConfigKT::class,
    SiteBWanEventSubstitutionFilterServerConfigKT::class, WanEventSubstitutionFilterKT::class)
class WanEventSubstitutionFilterConfigKT
