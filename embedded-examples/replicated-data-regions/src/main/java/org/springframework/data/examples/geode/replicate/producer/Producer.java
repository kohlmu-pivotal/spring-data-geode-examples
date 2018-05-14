package org.springframework.data.examples.geode.replicate.producer;


import javax.annotation.Resource;

import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.examples.geode.domain.Customer;
import org.springframework.data.examples.geode.domain.EmailAddress;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;

@SpringBootApplication
@PeerCacheApplication(locators = "localhost[10334]", name = "ProducerPeer", logLevel = "warn")
public class Producer {

	@Resource
	private Region<Long, Customer> customerRegion;

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(Producer.class, args);
		Producer producer = applicationContext.getBean(Producer.class);
		producer.customerRegion.put(1L, new Customer(1L, new EmailAddress("2@2.com"), "Me", "My"));
		producer.customerRegion.put(2L, new Customer(2L, new EmailAddress("3@3.com"), "You", "Yours"));
	}

	@Bean("customerRegion")
	ReplicatedRegionFactoryBean<Long, Customer> customerRegion(GemFireCache gemfireCache) {
		ReplicatedRegionFactoryBean replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean();
		replicatedRegionFactoryBean.setCache(gemfireCache);
		replicatedRegionFactoryBean.setRegionName("Customer");
		replicatedRegionFactoryBean.setDataPolicy(DataPolicy.EMPTY);
		return replicatedRegionFactoryBean;
	}
}
