package org.springframework.data.examples.geode.basic.server;

import java.io.IOException;
import java.util.Scanner;

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
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.EnableLocator;

@SpringBootApplication(scanBasePackages = "org.springframework.data.examples.geode.basic.server")
@EnableLocator
@CacheServerApplication()
@SuppressWarnings({ "raw" })
public class Server {

	@Resource
	private Region<String, Customer> customerRegion;

	public static void main(String[] args) throws IOException {
		SpringApplication.run(Server.class, args);
		System.err.println("Press <ENTER> to exit");
		new Scanner(System.in).nextLine();
	}

	@Bean LoggingCacheListener loggingCacheListener() {
		return new LoggingCacheListener();
	}

	@Bean("customerRegion")
	ReplicatedRegionFactoryBean<String, Customer> customerRegion(GemFireCache gemfireCache) {
		ReplicatedRegionFactoryBean replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean();
		replicatedRegionFactoryBean.setCache(gemfireCache);
		replicatedRegionFactoryBean.setRegionName("Customer");
		replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
		replicatedRegionFactoryBean.setCacheListeners(new CacheListener[] { loggingCacheListener() });
		return replicatedRegionFactoryBean;
	}
}
