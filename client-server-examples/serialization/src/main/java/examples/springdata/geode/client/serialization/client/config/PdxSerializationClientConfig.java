package examples.springdata.geode.client.serialization.client.config;

import examples.springdata.geode.client.serialization.client.repo.CustomerRepository;
import examples.springdata.geode.client.serialization.client.services.CustomerService;
import examples.springdata.geode.domain.Customer;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@EnablePdx
@ComponentScan(basePackageClasses = CustomerService.class)
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@ClientCacheApplication(name = "PDXSerializedClient", logLevel = "error", pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
public class PdxSerializationClientConfig {

	@Bean("Customers")
	protected ClientRegionFactoryBean<Long, Customer> configureProxyClientCustomerRegion(GemFireCache gemFireCache) {
		ClientRegionFactoryBean<Long, Customer> clientRegionFactoryBean = new ClientRegionFactoryBean<>();
		clientRegionFactoryBean.setCache(gemFireCache);
		clientRegionFactoryBean.setName("Customers");
		clientRegionFactoryBean.setShortcut(ClientRegionShortcut.PROXY);
		return clientRegionFactoryBean;
	}
}