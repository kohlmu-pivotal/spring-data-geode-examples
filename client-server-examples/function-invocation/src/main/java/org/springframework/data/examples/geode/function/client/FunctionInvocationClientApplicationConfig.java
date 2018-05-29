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

package org.springframework.data.examples.geode.function.client;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.examples.geode.common.client.ClientApplicationConfig;
import org.springframework.data.examples.geode.function.client.functions.CustomerFunctionExecutions;
import org.springframework.data.examples.geode.function.client.repo.CustomerRepository;
import org.springframework.data.examples.geode.model.Order;
import org.springframework.data.examples.geode.model.Product;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctionExecutions;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

/**
 * Spring JavaConfig configuration class to setup a Spring container and infrastructure components.
 *
 * @author Udo Kohlmeyer
 */
@Configuration
@Import(ClientApplicationConfig.class)
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@EnableGemfireFunctionExecutions(basePackageClasses = CustomerFunctionExecutions.class)
public class FunctionInvocationClientApplicationConfig extends ClientApplicationConfig {

	@Bean("customerTemplate")
	@DependsOn(ClientApplicationConfig.CUSTOMER_REGION_NAME)
	protected GemfireTemplate configureCustomerTemplate(GemFireCache gemfireCache) {
		return new GemfireTemplate(gemfireCache.getRegion(CUSTOMER_REGION_NAME));
	}

	@Bean("Products")
	@Profile({ "proxy", "default" })
	protected ClientRegionFactoryBean<Long, Product> configureProxyClientProductRegion(GemFireCache gemFireCache) {
		ClientRegionFactoryBean<Long, Product> clientRegionFactoryBean = new ClientRegionFactoryBean<>();
		clientRegionFactoryBean.setCache(gemFireCache);
		clientRegionFactoryBean.setName("Products");
		clientRegionFactoryBean.setShortcut(ClientRegionShortcut.PROXY);
		return clientRegionFactoryBean;
	}

	@Bean("Products")
	@Profile("localCache")
	protected ClientRegionFactoryBean<Long, Product> configureLocalCachingClientProductRegion(
		GemFireCache gemFireCache) {
		ClientRegionFactoryBean<Long, Product> clientRegionFactoryBean = new ClientRegionFactoryBean<>();
		clientRegionFactoryBean.setCache(gemFireCache);
		clientRegionFactoryBean.setName("Products");
		clientRegionFactoryBean.setShortcut(ClientRegionShortcut.CACHING_PROXY);
		return clientRegionFactoryBean;
	}

	@Bean("Orders")
	@Profile({ "proxy", "default" })
	protected ClientRegionFactoryBean<Long, Order> configureProxyClientOrderRegion(GemFireCache gemFireCache) {
		ClientRegionFactoryBean<Long, Order> clientRegionFactoryBean = new ClientRegionFactoryBean<>();
		clientRegionFactoryBean.setCache(gemFireCache);
		clientRegionFactoryBean.setName("Orders");
		clientRegionFactoryBean.setShortcut(ClientRegionShortcut.PROXY);
		return clientRegionFactoryBean;
	}

	@Bean("Orders")
	@Profile("localCache")
	protected ClientRegionFactoryBean<Long, Order> configureLocalCachingClientOrderRegion(
		GemFireCache gemFireCache) {
		ClientRegionFactoryBean<Long, Order> clientRegionFactoryBean = new ClientRegionFactoryBean<>();
		clientRegionFactoryBean.setCache(gemFireCache);
		clientRegionFactoryBean.setName("Orders");
		clientRegionFactoryBean.setShortcut(ClientRegionShortcut.CACHING_PROXY);
		return clientRegionFactoryBean;
	}
}
