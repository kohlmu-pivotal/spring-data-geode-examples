package org.springframework.data.examples.geode.replicate.consumer;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.examples.geode.domain.Customer;
import org.springframework.data.examples.geode.util.LoggingCacheListener;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;

@SpringBootApplication
@PeerCacheApplication(name = "ConsumerPeer", logLevel = "error")
@EnableLocator(port = 10334, host = "localhost")
public class Consumer {

	@Resource
	private Region<String, Customer> customerRegion;

	public static void main(String[] args) throws IOException {
		SpringApplication.run(Consumer.class, args);
		System.in.read();
	}

	@Bean LoggingCacheListener loggingCacheListener() {
		return new LoggingCacheListener();
	}

	@Bean("customerRegion")
	ReplicatedRegionFactoryBean<String, Customer> customerRegion(GemFireCache gemfireCache) {
		ReplicatedRegionFactoryBean replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean();
		replicatedRegionFactoryBean.setCache(gemfireCache);
		replicatedRegionFactoryBean.setRegionName("customerRegion");
		replicatedRegionFactoryBean.setCacheListeners(new CacheListener[] { loggingCacheListener() });
		replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
		return replicatedRegionFactoryBean;
	}
}