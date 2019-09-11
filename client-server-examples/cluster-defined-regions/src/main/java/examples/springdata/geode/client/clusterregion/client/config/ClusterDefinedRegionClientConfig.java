package examples.springdata.geode.client.clusterregion.client.config;

import examples.springdata.geode.client.clusterregion.client.repo.CustomerRepository;
import examples.springdata.geode.client.clusterregion.client.service.CustomerService;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableClusterDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableIndexing;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@ClientCacheApplication(logLevel = "error")
@EnableClusterDefinedRegions(clientRegionShortcut = ClientRegionShortcut.PROXY)
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@ComponentScan(basePackageClasses = CustomerService.class)
@EnableIndexing
@EnableClusterConfiguration
public class ClusterDefinedRegionClientConfig {
}