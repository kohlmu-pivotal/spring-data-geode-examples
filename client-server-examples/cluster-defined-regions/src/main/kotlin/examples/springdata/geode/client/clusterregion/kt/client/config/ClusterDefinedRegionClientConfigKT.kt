package examples.springdata.geode.client.clusterregion.kt.client.config

import examples.springdata.geode.client.clusterregion.kt.client.repo.CustomerRepositoryKT
import examples.springdata.geode.client.clusterregion.kt.client.service.CustomerServiceKT
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration
import org.springframework.data.gemfire.config.annotation.EnableClusterDefinedRegions
import org.springframework.data.gemfire.config.annotation.EnableIndexing
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@ClientCacheApplication
@EnableClusterDefinedRegions(clientRegionShortcut = ClientRegionShortcut.PROXY)
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@ComponentScan(basePackageClasses = [CustomerServiceKT::class])
@EnableIndexing
@EnableClusterConfiguration
class ClusterDefinedRegionClientConfigKT