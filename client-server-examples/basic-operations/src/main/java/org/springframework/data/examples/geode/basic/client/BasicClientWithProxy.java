package org.springframework.data.examples.geode.basic.client;

import java.util.Collections;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.examples.geode.domain.Customer;
import org.springframework.data.examples.geode.domain.EmailAddress;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.ClientCacheConfiguration;
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer;

@SpringBootApplication(scanBasePackages = "org.springframework.data.examples.geode.basic.client")
public class BasicClientWithProxy {

	private final String CUSTOMER_REGION_NAME = "customerRegion";
	@Resource
	private Region<String, Customer> clientCustomerRegion;

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(BasicClientWithProxy.class, args);
		BasicClientWithProxy client = applicationContext.getBean(BasicClientWithProxy.class);

		System.out.println("Inserting 3 entries for keys: \"1\", \"2\",\"3\"");
		Region<String, Customer> clientCustomerRegion = client.clientCustomerRegion;
		clientCustomerRegion.put("1", new Customer(123L, new EmailAddress("2@2.com"), "Me", "My"));
		clientCustomerRegion.put("2", new Customer(1234L, new EmailAddress("3@3.com"), "You", "Yours"));
		clientCustomerRegion.put("3", new Customer(9876L, new EmailAddress("5@5.com"), "Third", "Entry"));

		Set<String> keysOnServer = clientCustomerRegion.keySetOnServer();

		System.out.println("Entries on Client: " + clientCustomerRegion.size());
		System.out.println("Entries on Server: " + keysOnServer.size());
		keysOnServer.forEach(
			entry -> System.out
				.println("\t Entry: \n \t\t Key: " + entry + " \n \t\t Value: " + clientCustomerRegion.get(entry)));

		System.out.println("Updating entry for key: \"2\"");
		System.out.println("Entry Before: " + clientCustomerRegion.get("2"));
		clientCustomerRegion.put("2", new Customer(456L, new EmailAddress("4@4.com"), "First", "Update"));
		System.out.println("Entry After: " + clientCustomerRegion.get("2"));

		System.out.println("Removing entry for key: \"3\"");
		clientCustomerRegion.remove("3");

		System.out.println("Entries:");
		clientCustomerRegion.keySetOnServer().forEach(
			entry -> System.out
				.println("\t Entry: \n \t\t Key: " + entry + " \n \t\t Value: " + clientCustomerRegion.get(entry)));
	}

	@Bean("clientCustomerRegion")
	protected ClientRegionFactoryBean<String, Customer> configureClientCustomerRegion(GemFireCache gemFireCache) {
		ClientRegionFactoryBean clientRegionFactoryBean = new ClientRegionFactoryBean();
		clientRegionFactoryBean.setCache(gemFireCache);
		clientRegionFactoryBean.setName(CUSTOMER_REGION_NAME);
		clientRegionFactoryBean.setShortcut(ClientRegionShortcut.PROXY);
		return clientRegionFactoryBean;
	}

	@ClientCacheApplication(name = "BasicClientWithProxy", logLevel = "error",
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
