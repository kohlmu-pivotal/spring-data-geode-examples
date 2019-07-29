package examples.springdata.geode.client.entityregion.client.config;

import examples.springdata.geode.client.entityregion.client.repo.CustomerRepository;
import examples.springdata.geode.client.entityregion.client.service.CustomerService;
import examples.springdata.geode.domain.Customer;
import org.apache.geode.cache.RegionShortcut;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableIndexing;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@ClientCacheApplication
@EnableEntityDefinedRegions(basePackageClasses = Customer.class,
        clientRegionShortcut = ClientRegionShortcut.PROXY,
        serverRegionShortcut = RegionShortcut.PARTITION)
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@ComponentScan(basePackageClasses = CustomerService.class)
@EnableIndexing
@EnableClusterConfiguration
public class EntityDefinedRegionClientConfig {
}