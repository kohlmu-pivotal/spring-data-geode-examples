package examples.springdata.geode.client.kt.entityregion.client.config

import examples.springdata.geode.client.kt.entityregion.client.repo.CustomerRepositoryKT
import examples.springdata.geode.client.kt.entityregion.client.service.CustomerServiceKT
import examples.springdata.geode.domain.Customer
import org.apache.geode.cache.RegionShortcut
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@ClientCacheApplication(logLevel = "error")
@EnableEntityDefinedRegions(basePackageClasses = [Customer::class],
        clientRegionShortcut = ClientRegionShortcut.PROXY,
        serverRegionShortcut = RegionShortcut.PARTITION)
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@ComponentScan(basePackageClasses = [CustomerServiceKT::class])
@EnableClusterConfiguration
class EntityDefinedRegionClientConfigKT {
}