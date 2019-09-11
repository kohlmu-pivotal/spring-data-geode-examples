package examples.springdata.geode.server.wan.event.kt.config

import examples.springdata.geode.server.wan.event.kt.filters.EvenNumberedKeyWanEventFilterKT
import examples.springdata.geode.server.wan.kt.config.WanEnabledServerCommonConfigKT
import examples.springdata.geode.server.wan.kt.server.siteA.config.SiteAWanEnabledServerConfigKT
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


@Configuration
@Import(WanEnabledServerCommonConfigKT::class, SiteBWanEventFilteringServerConfigKT::class,
        SiteAWanEnabledServerConfigKT::class, EvenNumberedKeyWanEventFilterKT::class)
class WanEventFiltersConfigKT