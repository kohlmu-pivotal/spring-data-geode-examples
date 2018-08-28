package examples.springdata.geode.kt.server.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.config.annotation.*

@Configuration
@EnableLocator(host = "localhost", port = 10334)
@EnableManager(start = true)
@EnableHttpService(startDeveloperRestApi = true, port = 7070)
@CacheServerApplication(port = 0, logLevel = "info", useClusterConfiguration = true)
@EnableClusterConfiguration(useHttp = true)
class EntityDefinedRegionServerConfigKT