package examples.springdata.geode.wan.substitution.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import examples.springdata.geode.server.wan.config.SiteAWanEnabledServerConfig;
import examples.springdata.geode.server.wan.config.WanEnabledServerCommonConfig;
import examples.springdata.geode.wan.substitution.filter.WanEventSubstitutionFilter;

@Configuration
@Import({WanEnabledServerCommonConfig.class, SiteAWanEnabledServerConfig.class,
        SiteBWanEventSubstitutionFilterServerConfig.class, WanEventSubstitutionFilter.class})
public class WanEventSubstitutionFilterConfig {
}
