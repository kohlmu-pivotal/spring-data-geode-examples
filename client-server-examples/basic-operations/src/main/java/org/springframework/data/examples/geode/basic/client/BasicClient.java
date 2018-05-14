package org.springframework.data.examples.geode.basic.client;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.examples.geode.basic.repository.CustomerRepository;
import org.springframework.data.examples.geode.domain.Customer;
import org.springframework.data.examples.geode.domain.EmailAddress;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.ClientCacheConfiguration;
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@SpringBootApplication(scanBasePackages = "org.springframework.data.examples.geode.basic.client")
@EnableGemfireRepositories(basePackages = "org.springframework.data.examples.geode.basic.repository")
public class BasicClient {

	private final String CUSTOMER_REGION_NAME = "Customer";
	private final String CUSTOMER_REGION_BEAN_NAME = "clientCustomerRegion";

	@Resource
	@Qualifier(CUSTOMER_REGION_BEAN_NAME)
	private Region<Long, Customer> clientCustomerRegion;

	@Autowired
	private CustomerRepository customerRepository;

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(BasicClient.class, args);
		BasicClient client = applicationContext.getBean(BasicClient.class);

		System.out.println("Inserting 3 entries for keys: 1, 2, 3");
		CustomerRepository customerRepository = client.customerRepository;
		customerRepository.save(new Customer(1L, new EmailAddress("2@2.com"), "Me", "My"));
		customerRepository.save(new Customer(2L, new EmailAddress("3@3.com"), "You", "Yours"));
		customerRepository.save(new Customer(3L, new EmailAddress("5@5.com"), "Third", "Entry"));

		List<Customer> customers = customerRepository.findAll();

		System.out.println("Entries on Client: " + client.clientCustomerRegion.size());
		System.out.println("Entries on Server: " + client.clientCustomerRegion.keySetOnServer().size());
		customers.forEach(customer -> System.out.println("\t Entry: \n \t\t " + customer));

		System.out.println("Updating entry for key: 2");
		System.out.println("Entry Before: " + customerRepository.findById(2L).get());
		customerRepository.save(new Customer(2L, new EmailAddress("4@4.com"), "First", "Update"));
		System.out.println("Entry After: " + customerRepository.findById(2L).get());

		System.out.println("Removing entry for key: 3");
		customerRepository.deleteById(3L);

		System.out.println("Entries:");
		customers = customerRepository.findAll();
		customers.forEach(customer -> System.out.println("\t Entry: \n \t\t " + customer));
	}

	@Bean(CUSTOMER_REGION_BEAN_NAME)
	@Profile("proxy")
	protected ClientRegionFactoryBean<Long, Customer> configureProxyClientCustomerRegion(GemFireCache gemFireCache) {
		ClientRegionFactoryBean clientRegionFactoryBean = new ClientRegionFactoryBean();
		clientRegionFactoryBean.setCache(gemFireCache);
		clientRegionFactoryBean.setName(CUSTOMER_REGION_NAME);
		clientRegionFactoryBean.setShortcut(ClientRegionShortcut.PROXY);
		return clientRegionFactoryBean;
	}

	@Bean(CUSTOMER_REGION_BEAN_NAME)
	@Profile("localCache")
	protected ClientRegionFactoryBean<Long, Customer> configureLocalCacheClientCustomerRegion(
		GemFireCache gemFireCache) {
		ClientRegionFactoryBean clientRegionFactoryBean = new ClientRegionFactoryBean();
		clientRegionFactoryBean.setCache(gemFireCache);
		clientRegionFactoryBean.setName(CUSTOMER_REGION_NAME);
		clientRegionFactoryBean.setShortcut(ClientRegionShortcut.CACHING_PROXY);
		return clientRegionFactoryBean;
	}

	@ClientCacheApplication(name = "BasicClient", logLevel = "error",
		pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
	static class ReplicateClientCacheConfiguration extends ClientCacheConfiguration {

		// Required to resolve property placeholders in Spring @Value annotations.
		@Bean
		static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
			return new PropertySourcesPlaceholderConfigurer();
		}

		@Bean
		ClientCacheConfigurer clientCacheServerConfigurer(
			@Value("${spring.session.data.geode.cache.server.host:localhost}") String hostname,
			@Value("${spring.session.data.geode.cache.server.port:40404}") int port) {

			return (beanName, clientCacheFactoryBean) -> clientCacheFactoryBean.setServers(Collections.singletonList(
				newConnectionEndpoint(hostname, port)));
		}
	}
}
