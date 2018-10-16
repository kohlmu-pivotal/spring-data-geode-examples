package examples.springdata.geode.server.wan.filters.event.config;


import examples.springdata.geode.server.wan.config.SiteAWanEnabledServerConfig;
import examples.springdata.geode.server.wan.config.WanEnabledServerCommonConfig;
import examples.springdata.geode.server.wan.filters.event.filters.EvenNumberedKeyWanEventFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WanEnabledServerCommonConfig.class, SiteAWanEnabledServerConfig.class,
        SiteBWanEventFilteringServerConfig.class, EvenNumberedKeyWanEventFilter.class})
public class WanEventFiltersConfig {
}
