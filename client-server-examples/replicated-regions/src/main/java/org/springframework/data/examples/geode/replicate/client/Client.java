package org.springframework.data.examples.geode.replicate.client;

import java.util.Collections;

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

@SpringBootApplication
public class Client {

	@Resource
	private Region<String, Customer> clientCustomerRegion;

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(Client.class, args);
		Client client = applicationContext.getBean(Client.class);
		client.clientCustomerRegion.put("1", new Customer(123L, new EmailAddress("2@2.com"), "Me", "My"));
		client.clientCustomerRegion.put("2", new Customer(1234L, new EmailAddress("3@3.com"), "You", "Yours"));
	}

	@Bean("clientCustomerRegion")
	protected ClientRegionFactoryBean<String, Customer> configureClientCustomerRegion(GemFireCache gemFireCache) {
		ClientRegionFactoryBean clientRegionFactoryBean = new ClientRegionFactoryBean();
		clientRegionFactoryBean.setCache(gemFireCache);
		clientRegionFactoryBean.setName("customerRegion");
		clientRegionFactoryBean.setShortcut(ClientRegionShortcut.PROXY);
		return clientRegionFactoryBean;
	}

	@ClientCacheApplication(name = "ReplicateRegionClient", logLevel = "error",
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
