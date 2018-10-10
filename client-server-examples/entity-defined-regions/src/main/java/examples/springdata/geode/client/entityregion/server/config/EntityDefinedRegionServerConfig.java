package examples.springdata.geode.client.entityregion.server.config;

import org.springframework.data.gemfire.config.annotation.*;

@EnableLocator
@EnableManager(start = true)
@EnableHttpService(startDeveloperRestApi = true)
@CacheServerApplication(port = 0, logLevel = "info", useClusterConfiguration = true)
public class EntityDefinedRegionServerConfig {
}
