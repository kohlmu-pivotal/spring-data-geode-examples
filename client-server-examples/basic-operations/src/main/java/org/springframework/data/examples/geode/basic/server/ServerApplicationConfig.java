package org.springframework.data.examples.geode.basic.server;

import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.examples.geode.domain.Customer;
import org.springframework.data.examples.geode.util.LoggingCacheListener;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.EnableLocator;

@Configuration
@EnableLocator
@CacheServerApplication
public class ServerApplicationConfig {

	@Bean LoggingCacheListener loggingCacheListener() {
		return new LoggingCacheListener();
	}

	@Bean("customerRegion")
	ReplicatedRegionFactoryBean<Long, Customer> customerRegion(GemFireCache gemfireCache) {
		ReplicatedRegionFactoryBean replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean();
		replicatedRegionFactoryBean.setCache(gemfireCache);
		replicatedRegionFactoryBean.setRegionName("Customer");
		replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
		replicatedRegionFactoryBean.setCacheListeners(new CacheListener[] { loggingCacheListener() });
		return replicatedRegionFactoryBean;
	}

}
