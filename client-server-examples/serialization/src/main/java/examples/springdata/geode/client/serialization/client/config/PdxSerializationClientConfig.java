package examples.springdata.geode.client.serialization.client.config;

import java.util.Collections;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.support.ConnectionEndpoint;

import examples.springdata.geode.client.serialization.client.services.CustomerService;
import examples.springdata.geode.domain.Customer;

@EnablePdx
@ComponentScan(basePackageClasses = CustomerService.class)
@ClientCacheApplication(name = "PDXSerializedClient", logLevel = "error", pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
public class PdxSerializationClientConfig {

	@Bean("Customers")
	@Profile({ "default" })
	protected ClientRegionFactoryBean<Long, Customer> configureProxyClientCustomerRegion(GemFireCache gemFireCache) {
		ClientRegionFactoryBean<Long, Customer> clientRegionFactoryBean = new ClientRegionFactoryBean<>();
		clientRegionFactoryBean.setCache(gemFireCache);
		clientRegionFactoryBean.setName("Customers");
		clientRegionFactoryBean.setShortcut(ClientRegionShortcut.PROXY);
		return clientRegionFactoryBean;
	}

	@Bean
	ClientCacheConfigurer clientCacheServerConfigurer(
		@Value("${spring.data.geode.locator.host:localhost}") String hostname,
		@Value("${spring.data.geode.locator.port:10334}") int port) {

		return (beanName, clientCacheFactoryBean) -> clientCacheFactoryBean.setLocators(Collections.singletonList(
			new ConnectionEndpoint(hostname, port)));
	}
}
