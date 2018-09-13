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

package examples.springdata.geode.client.function.kt.client.config

import examples.springdata.geode.client.function.kt.client.functions.CustomerFunctionExecutionsKT
import examples.springdata.geode.client.function.kt.client.repo.CustomerRepositoryKT
import examples.springdata.geode.client.function.kt.client.services.CustomerServiceKT
import examples.springdata.geode.common.client.kt.ClientApplicationConfigKT
import examples.springdata.geode.domain.Order
import examples.springdata.geode.domain.Product
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.context.annotation.*
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.function.config.EnableGemfireFunctionExecutions
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

/**
 * Spring JavaConfig configuration class to setup a Spring container and infrastructure components.
 *
 * @author Udo Kohlmeyer
 */
@Configuration
@Import(ClientApplicationConfigKT::class)
@ComponentScan(basePackageClasses = [CustomerServiceKT::class])
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@EnableGemfireFunctionExecutions(basePackageClasses = [CustomerFunctionExecutionsKT::class])
class FunctionInvocationClientApplicationConfigKT {

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