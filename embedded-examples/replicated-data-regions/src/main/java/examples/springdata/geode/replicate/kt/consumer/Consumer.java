package examples.springdata.geode.replicate.kt.consumer;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.util.LoggingCacheListener;
import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
@PeerCacheApplication(name = "ConsumerPeer", logLevel = "error")
@EnableLocator
public class Consumer {

	@Resource
	private Region<Long, Customer> customerRegion;

	public static void main(String[] args) throws IOException {
		SpringApplication.run(Consumer.class, args);
		System.err.println("Press <ENTER> to exit");
		new Scanner(System.in).nextLine();
	}

	@Bean
	LoggingCacheListener loggingCacheListener() {
		return new LoggingCacheListener();
	}

	@Bean
	ReplicatedRegionFactoryBean<Long, Customer> customerRegion(GemFireCache gemfireCache) {
		ReplicatedRegionFactoryBean replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean();
		replicatedRegionFactoryBean.setCache(gemfireCache);
		replicatedRegionFactoryBean.setRegionName("Customers");
		replicatedRegionFactoryBean.setCacheListeners(new CacheListener[] { loggingCacheListener() });
		replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
		return replicatedRegionFactoryBean;
	}
}