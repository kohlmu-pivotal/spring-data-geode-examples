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

package org.springframework.data.examples.geode.basic.kt.client

import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.data.examples.geode.domain.Customer
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.ClientCacheConfiguration
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

/**
 * Spring JavaConfig configuration class to setup a Spring container and infrastructure components.
 *
 * @author Udo Kohlmeyer
 */
@Configuration
@EnableGemfireRepositories(basePackages = ["org.springframework.data.examples.geode.basic.kt.repository"])
class BasicClientApplicationConfigKT {

    @Bean(CUSTOMER_REGION_BEAN_NAME)
    @Profile("proxy")
    internal fun configureProxyClientCustomerRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<String, Customer>()
        .apply {
            cache = gemFireCache
            setName(CUSTOMER_REGION_NAME)
            setShortcut(ClientRegionShortcut.PROXY)
        }


    @Bean(CUSTOMER_REGION_BEAN_NAME)
    @Profile("localCache")
    internal fun configureLocalCachingClientCustomerRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<String, Customer>()
        .apply {
            cache = gemFireCache
            setName(CUSTOMER_REGION_NAME)
            setShortcut(ClientRegionShortcut.CACHING_PROXY)
        }

    @ClientCacheApplication(name = "BasicClientKT", logLevel = "error", pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
    internal class BasicClientConfiguration : ClientCacheConfiguration() {

        @Bean
        internal fun clientCacheServerConfigurer(
            @Value("\${spring.session.data.geode.cache.server.host:localhost}") hostname: String,
            @Value("\${spring.session.data.geode.cache.server.port:40404}") port: Int) =
            ClientCacheConfigurer { _, clientCacheFactoryBean ->
                clientCacheFactoryBean.setServers(listOf(newConnectionEndpoint(hostname, port)))
            }

        companion object {
            // Required to resolve property placeholders in Spring @Value annotations.
            @Bean
            internal fun propertyPlaceholderConfigurer() = PropertySourcesPlaceholderConfigurer()
        }
    }

    companion object {
        internal const val CUSTOMER_REGION_BEAN_NAME = "customerRegion"
        private const val CUSTOMER_REGION_NAME = "Customer"
    }
}
