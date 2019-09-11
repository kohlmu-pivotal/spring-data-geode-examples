package examples.springdata.geode.server.wan.transport.config;

import examples.springdata.geode.server.wan.config.WanEnabledServerCommonConfig;
import examples.springdata.geode.server.wan.server.siteA.config.SiteAWanEnabledServerConfig;
import examples.springdata.geode.server.wan.transport.transport.WanTransportEncryptionListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WanEnabledServerCommonConfig.class, SiteAWanEnabledServerConfig.class,
        SiteBWanTransportListenerServerConfig.class, WanTransportEncryptionListener.class})
public class WanTransportListenerConfig {
}
