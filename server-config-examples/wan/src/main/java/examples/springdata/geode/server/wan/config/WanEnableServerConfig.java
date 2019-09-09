package examples.springdata.geode.server.wan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WanEnabledServerCommonConfig.class, SiteAWanEnabledServerConfig.class, SiteBWanEnabledServerConfig.class})
public class WanEnableServerConfig {
}