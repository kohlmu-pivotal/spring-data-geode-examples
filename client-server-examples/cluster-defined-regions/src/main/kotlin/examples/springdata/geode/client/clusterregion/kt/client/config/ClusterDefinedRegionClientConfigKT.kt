package examples.springdata.geode.client.clusterregion.kt.client.config

import examples.springdata.geode.client.clusterregion.kt.client.repo.CustomerRepositoryKT
import examples.springdata.geode.client.clusterregion.kt.client.service.CustomerServiceKT
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.gemfire.config.annotation.*
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.gemfire.support.ConnectionEndpoint

@ClientCacheApplication
@EnableClusterDefinedRegions(clientRegionShortcut = ClientRegionShortcut.PROXY)
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@ComponentScan(basePackageClasses = [CustomerServiceKT::class])
@EnableIndexing
@EnableClusterConfiguration
class ClusterDefinedRegionClientConfigKT {
    @Bean
    internal fun clientCacheServerConfigurer(
            @Value("\${spring.data.geode.locator.host:localhost}") hostname: String,
            @Value("\${spring.data.geode.locator.port:10334}") port: Int) =
            ClientCacheConfigurer { _, clientCacheFactoryBean ->
                clientCacheFactoryBean.setLocators(listOf(ConnectionEndpoint(hostname, port)))
            }
}