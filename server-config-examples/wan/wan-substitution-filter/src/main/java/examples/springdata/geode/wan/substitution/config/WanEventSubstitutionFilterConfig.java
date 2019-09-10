package examples.springdata.geode.wan.substitution.config;


import examples.springdata.geode.server.wan.config.WanEnabledServerCommonConfig;
import examples.springdata.geode.server.wan.server.siteA.config.SiteAWanEnabledServerConfig;
import examples.springdata.geode.wan.substitution.filter.WanEventSubstitutionFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WanEnabledServerCommonConfig.class, SiteAWanEnabledServerConfig.class,
        SiteBWanEventSubstitutionFilterServerConfig.class, WanEventSubstitutionFilter.class})
public class WanEventSubstitutionFilterConfig {
}
