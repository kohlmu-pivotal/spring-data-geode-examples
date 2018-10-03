package examples.springdata.geode.client.serialization.server.config;/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Scope;
import org.apache.geode.pdx.PdxInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.EnablePdx;

import examples.springdata.geode.util.LoggingCacheListener;

@Profile("readSerialized")
@EnableLocator
@EnablePdx(readSerialized = true)
@CacheServerApplication(port = 0, logLevel = "info")
public class ReadSerializedServerApplicationConfig {

	@Bean("loggingCacheListener")
	@Profile("readSerialized")
	LoggingCacheListener<Long, PdxInstance> getLoggingCacheListener() {
		return new LoggingCacheListener<>();
	}

	@Bean("Customers")
	@Profile("readSerialized")
	protected ReplicatedRegionFactoryBean customerRegion(GemFireCache gemfireCache) {
		ReplicatedRegionFactoryBean<Long, PdxInstance> replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean<>();
		replicatedRegionFactoryBean.setCache(gemfireCache);
		replicatedRegionFactoryBean.setRegionName("Customers");
		replicatedRegionFactoryBean.setScope(Scope.DISTRIBUTED_ACK);
		replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
		replicatedRegionFactoryBean.setCacheListeners(new LoggingCacheListener[] { getLoggingCacheListener() });
		return replicatedRegionFactoryBean;
	}
}
