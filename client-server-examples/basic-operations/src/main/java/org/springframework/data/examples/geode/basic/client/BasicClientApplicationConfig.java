/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.data.examples.geode.basic.client;

import java.util.Collections;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.examples.geode.domain.Customer;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.ClientCacheConfiguration;
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

/**
 * Spring JavaConfig configuration class to setup a Spring container and infrastructure components.
 *
 * @author Udo Kohlmeyer
 */
@Configuration
@EnableGemfireRepositories(basePackages = "org.springframework.data.examples.geode.basic.repository")
public class BasicClientApplicationConfig {

	static final String CUSTOMER_REGION_BEAN_NAME = "customerRegion";
	private static final String CUSTOMER_REGION_NAME = "Customer";

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

	@ClientCacheApplication(name = "BasicClient", logLevel = "error", pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
	@Configuration
	static class ReplicateClientCacheConfiguration extends ClientCacheConfiguration {

		// Required to resolve property placeholders in Spring @Value annotations.
		@Bean
		static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
			return new PropertySourcesPlaceholderConfigurer();
		}

		@Bean
		ClientCacheConfigurer clientCacheServerConfigurer(
			@Value("${spring.session.data.geode.locator.host:localhost}") String hostname,
			@Value("${spring.session.data.geode.locator.port:10334}") int port) {

			return (beanName, clientCacheFactoryBean) -> clientCacheFactoryBean.setLocators(Collections.singletonList(
				newConnectionEndpoint(hostname, port)));
		}
	}
}
