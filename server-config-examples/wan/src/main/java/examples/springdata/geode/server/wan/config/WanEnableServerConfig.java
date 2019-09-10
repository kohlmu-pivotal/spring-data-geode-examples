package examples.springdata.geode.server.wan.config;

import examples.springdata.geode.server.wan.server.siteA.config.SiteAWanEnabledServerConfig;
import examples.springdata.geode.server.wan.server.siteB.config.SiteBWanEnabledServerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WanEnabledServerCommonConfig.class, SiteAWanEnabledServerConfig.class, SiteBWanEnabledServerConfig.class})
public class WanEnableServerConfig {
}
