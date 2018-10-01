package examples.springdata.geode.entityregion.server.config;

import org.springframework.data.gemfire.config.annotation.*;

@EnableLocator
@EnableManager(start = true)
@EnableHttpService
//@EnableHttpService(startDeveloperRestApi = true)
@CacheServerApplication(port = 0, logLevel = "info", useClusterConfiguration = true)
public class EntityDefinedRegionServerConfig {
}
