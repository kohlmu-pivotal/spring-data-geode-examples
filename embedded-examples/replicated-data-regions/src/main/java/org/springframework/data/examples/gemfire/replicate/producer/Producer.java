package org.springframework.data.examples.gemfire.replicate.producer;


import javax.annotation.Resource;

import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.gemfire.examples.domain.Customer;
import org.springframework.data.gemfire.examples.domain.EmailAddress;

@SpringBootApplication
@PeerCacheApplication(locators = "localhost[10334]", name = "ProducerPeer", logLevel = "warn")
public class Producer {

	@Resource
	private Region<String, Customer> customerRegion;

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(ProducerKT.class, args);
		ProducerKT producer = applicationContext.getBean(ProducerKT.class);
		producer.customerRegion.put("1", new Customer(123L, new EmailAddress("2@2.com"), "Me", "My"));
		producer.customerRegion.put("2", new Customer(1234L, new EmailAddress("3@3.com"), "You", "Yours"));
	}

	@Bean("customerRegion")
	ReplicatedRegionFactoryBean<String, Customer> customerRegion(GemFireCache gemfireCache) {
		ReplicatedRegionFactoryBean replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean();
		replicatedRegionFactoryBean.setCache(gemfireCache);
		replicatedRegionFactoryBean.setRegionName("customerRegion");
		replicatedRegionFactoryBean.setDataPolicy(DataPolicy.EMPTY);
		return replicatedRegionFactoryBean;
	}
}
