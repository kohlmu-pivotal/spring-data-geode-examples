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

package org.springframework.data.examples.geode.oql.client;

import org.apache.geode.cache.GemFireCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.examples.geode.common.client.ClientApplicationConfig;
import org.springframework.data.examples.geode.oql.client.repo.CustomerRepository;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

/**
 * Spring JavaConfig configuration class to setup a Spring container and infrastructure components.
 *
 * @author Udo Kohlmeyer
 */
@Configuration
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
public class OQLClientApplicationConfig extends ClientApplicationConfig {

	@Bean("customerTemplate")
	@DependsOn(ClientApplicationConfig.CUSTOMER_REGION_BEAN_NAME)
	protected GemfireTemplate configureCustomerTemplate(GemFireCache gemfireCache) {
		return new GemfireTemplate(gemfireCache.getRegion(CUSTOMER_REGION_NAME));
	}
}
