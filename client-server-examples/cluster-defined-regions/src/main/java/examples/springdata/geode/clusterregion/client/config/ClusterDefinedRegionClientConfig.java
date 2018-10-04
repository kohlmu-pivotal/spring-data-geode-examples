package examples.springdata.geode.entityregion.client.config;

import java.util.Collections;

import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableIndexing;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.support.ConnectionEndpoint;

import examples.springdata.geode.clusterregion.client.repo.CustomerRepository;
import examples.springdata.geode.clusterregion.client.service.CustomerService;

@ClientCacheApplication
@EnableCachingDefinedRegions(clientRegionShortcut = ClientRegionShortcut.PROXY)
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@ComponentScan(basePackageClasses = CustomerService.class)
@EnableIndexing
@EnableClusterConfiguration(useHttp = true)
public class ClusterDefinedRegionClientConfig {

	@Bean
	ClientCacheConfigurer clientCacheServerConfigurer(
		@Value("${spring.data.geode.locator.host:localhost}") String hostname,
		@Value("${spring.data.geode.locator.port:10334}") int port) {

		return (beanName, clientCacheFactoryBean) -> clientCacheFactoryBean
			.setLocators(Collections.singletonList(
				new ConnectionEndpoint(hostname, port)));
	}
}