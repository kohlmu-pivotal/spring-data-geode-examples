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

package examples.springdata.geode.function.client;

import examples.springdata.geode.common.client.ClientApplicationConfig;
import examples.springdata.geode.domain.Order;
import examples.springdata.geode.domain.Product;
import examples.springdata.geode.function.client.functions.CustomerFunctionExecutions;
import examples.springdata.geode.function.client.repo.CustomerRepository;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.*;
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
	@DependsOn("Customers")
	protected GemfireTemplate configureCustomerTemplate(GemFireCache gemfireCache) {
		return new GemfireTemplate(gemfireCache.getRegion("Customers"));
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
