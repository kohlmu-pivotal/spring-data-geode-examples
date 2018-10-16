package examples.springdata.geode.server.wan.filters.event.kt.config

import examples.springdata.geode.server.wan.filters.event.kt.filters.EvenNumberedKeyWanEventFilterKT
import examples.springdata.geode.server.wan.kt.server.siteA.WanEnabledServerSiteAKT
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


@Configuration
@Import(WanEnabledServerSiteAKT::class,
        SiteBWanEventFilteringServerConfigKT::class, EvenNumberedKeyWanEventFilterKT::class)
class WanEventFiltersConfigKT