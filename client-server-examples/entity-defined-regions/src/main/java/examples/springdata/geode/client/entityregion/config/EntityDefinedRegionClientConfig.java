package examples.springdata.geode.client.entityregion.config;

import examples.springdata.geode.client.entityregion.repo.CustomerRepository;
import examples.springdata.geode.client.entityregion.service.CustomerService;
import examples.springdata.geode.domain.Customer;
import org.apache.geode.cache.RegionShortcut;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.gemfire.config.annotation.*;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.support.ConnectionEndpoint;

import java.util.Collections;

@ClientCacheApplication
@EnableEntityDefinedRegions(basePackageClasses = Customer.class,
        clientRegionShortcut = ClientRegionShortcut.PROXY,
        serverRegionShortcut = RegionShortcut.PARTITION)
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@ComponentScan(basePackageClasses = CustomerService.class)
@EnableIndexing
@EnableClusterConfiguration
public class EntityDefinedRegionClientConfig {
    @Bean
    ClientCacheConfigurer clientCacheServerConfigurer(
            @Value("${spring.data.geode.locator.host:localhost}") String hostname,
            @Value("${spring.data.geode.locator.port:10334}") int port) {

        return (beanName, clientCacheFactoryBean) -> clientCacheFactoryBean
                .setLocators(Collections.singletonList(
                        new ConnectionEndpoint(hostname, port)));
    }
}