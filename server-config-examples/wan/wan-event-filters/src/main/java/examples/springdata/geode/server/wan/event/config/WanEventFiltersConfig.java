package examples.springdata.geode.server.wan.event.config;

import examples.springdata.geode.server.wan.config.WanEnabledServerCommonConfig;
import examples.springdata.geode.server.wan.event.filters.EvenNumberedKeyWanEventFilter;
import examples.springdata.geode.server.wan.server.siteA.config.SiteAWanEnabledServerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WanEnabledServerCommonConfig.class, SiteAWanEnabledServerConfig.class,
        SiteBWanEventFilteringServerConfig.class, EvenNumberedKeyWanEventFilter.class})
public class WanEventFiltersConfig {
}
