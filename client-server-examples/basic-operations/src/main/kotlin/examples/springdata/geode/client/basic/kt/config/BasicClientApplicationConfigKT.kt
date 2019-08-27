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

package examples.springdata.geode.client.basic.kt.config

import examples.springdata.geode.client.basic.kt.repo.CustomerRepositoryKT
import examples.springdata.geode.client.basic.kt.services.CustomerServiceKT
import examples.springdata.geode.domain.Customer
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

/**
 * Spring JavaConfig configuration class to setup a Spring container and infrastructure components.
 *
 * @author Udo Kohlmeyer
 * @author Patrick Johnson
 */
@Configuration
@ComponentScan(basePackageClasses = [CustomerServiceKT::class])
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@ClientCacheApplication(name = "DemoClientCache", logLevel = "error", pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
class BasicClientApplicationConfigKT {
    @Bean("Customers")
    protected fun configureProxyClientCustomerRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<Long, Customer>()
            .apply {
                cache = gemFireCache
                setName("Customers")
                setShortcut(ClientRegionShortcut.PROXY)
            }
}