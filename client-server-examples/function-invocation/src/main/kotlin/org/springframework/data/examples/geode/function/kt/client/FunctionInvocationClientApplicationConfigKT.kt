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

package org.springframework.data.examples.geode.function.kt.client

import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Profile
import org.springframework.data.examples.geode.common.kt.client.ClientApplicationConfigKT
import org.springframework.data.examples.geode.function.kt.client.functions.CustomerFunctionExecutionsKT
import org.springframework.data.examples.geode.function.kt.client.functions.OrderFunctionExecutionsKT
import org.springframework.data.examples.geode.function.kt.client.functions.ProductFunctionExecutionsKT
import org.springframework.data.examples.geode.function.kt.client.repo.CustomerRepositoryKT
import org.springframework.data.examples.geode.function.kt.client.repo.OrderRepositoryKT
import org.springframework.data.examples.geode.function.kt.client.repo.ProductRepositoryKT
import org.springframework.data.examples.geode.model.Customer
import org.springframework.data.examples.geode.model.Order
import org.springframework.data.examples.geode.model.Product
import org.springframework.data.gemfire.GemfireTemplate
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.function.config.EnableGemfireFunctionExecutions
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

/**
 * Spring JavaConfig configuration class to setup a Spring container and infrastructure components.
 *
 * @author Udo Kohlmeyer
 */
@Configuration
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class,
    OrderRepositoryKT::class, ProductRepositoryKT::class])
@EnableGemfireFunctionExecutions(basePackageClasses = [CustomerFunctionExecutionsKT::class,
    OrderFunctionExecutionsKT::class, ProductFunctionExecutionsKT::class])

class FunctionInvocationClientApplicationConfigKT : ClientApplicationConfigKT() {

    @Bean("customerTemplate")
    @DependsOn(CUSTOMER_REGION_NAME)
    internal fun configureCustomerTemplate(gemFireCache: GemFireCache) =
        GemfireTemplate<Long, Customer>(gemFireCache.getRegion(CUSTOMER_REGION_NAME)!!)

    @Bean("Products")
    @Profile("proxy", "default")
    protected fun configureProxyClientProductRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<Long, Product>()
        .apply {
            cache = gemFireCache
            setName("Products")
            setShortcut(ClientRegionShortcut.PROXY)
        }

    @Bean("Products")
    @Profile("localCache")
    protected fun configureLocalCachingClientProductRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<Long, Product>()
        .apply {
            cache = gemFireCache
            setName("Products")
            setShortcut(ClientRegionShortcut.CACHING_PROXY)
        }

    @Bean("Orders")
    @Profile("proxy", "default")
    protected fun configureProxyClientOrderRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<Long, Order>()
        .apply {
            cache = gemFireCache
            setName("Orders")
            setShortcut(ClientRegionShortcut.PROXY)
        }

    @Bean("Orders")
    @Profile("localCache")
    protected fun configureLocalCachingClientOrderRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<Long, Order>()
        .apply {
            cache = gemFireCache
            setName("Orders")
            setShortcut(ClientRegionShortcut.CACHING_PROXY)

        }
}