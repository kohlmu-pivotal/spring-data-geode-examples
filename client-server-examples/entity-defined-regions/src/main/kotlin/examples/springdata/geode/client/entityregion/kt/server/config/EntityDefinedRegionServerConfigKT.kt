package examples.springdata.geode.client.entityregion.kt.server.config

import org.springframework.data.gemfire.config.annotation.CacheServerApplication
import org.springframework.data.gemfire.config.annotation.EnableHttpService
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.EnableManager

@EnableLocator
@EnableManager(start = true)
@EnableHttpService(startDeveloperRestApi = true)
@CacheServerApplication(port = 0, logLevel = "error", useClusterConfiguration = true)
class EntityDefinedRegionServerConfigKT