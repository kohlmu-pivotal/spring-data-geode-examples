package examples.springdata.geode.server.wan.transport.config;

import examples.springdata.geode.server.wan.config.WanEnabledServerCommonConfig;
import examples.springdata.geode.server.wan.transport.transport.WanTransportEncryptionListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WanEnabledServerCommonConfig.class, SiteAWanTransportListenerServerConfig.class,
        SiteBWanTransportListenerServerConfig.class, WanTransportEncryptionListener.class})
public class WanTransportListenerConfig {
}
