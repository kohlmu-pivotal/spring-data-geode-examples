/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package examples.springdata.geode.wan.kt.substitution.config

import org.apache.geode.cache.Cache
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.wan.GatewayEventSubstitutionFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Profile
import org.springframework.data.gemfire.config.annotation.EnableGemFireProperties
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import org.springframework.data.gemfire.wan.GatewayReceiverFactoryBean
import org.springframework.data.gemfire.wan.GatewaySenderFactoryBean

@PeerCacheApplication
@Profile("SiteB")
@EnableLocator(port = 20334)
@EnableGemFireProperties(distributedSystemId = 2, remoteLocators = "localhost[10334]")
class SiteBWanEventSubstitutionFilterServerConfigKT {

    @Bean
    internal fun createGatewayReceiver(gemFireCache: GemFireCache) =
        GatewayReceiverFactoryBean(gemFireCache as Cache).apply {
            setStartPort(25000)
            setEndPort(25010)
        }

    @Bean
    @DependsOn("DiskStore")
    internal fun createGatewaySender(gemFireCache: GemFireCache, gatewayEventSubstitutionFilter: GatewayEventSubstitutionFilter<*, *>) =
        GatewaySenderFactoryBean(gemFireCache as Cache).apply {
            setBatchSize(15)
            setBatchTimeInterval(1000)
            setRemoteDistributedSystemId(1)
            setDiskStoreRef("DiskStore")
            setEventSubstitutionFilter(gatewayEventSubstitutionFilter)
            isPersistent = false
        }
}
