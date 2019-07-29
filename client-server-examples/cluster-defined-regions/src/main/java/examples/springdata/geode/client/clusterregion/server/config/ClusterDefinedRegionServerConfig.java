package examples.springdata.geode.client.clusterregion.server.config;

import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Scope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableHttpService;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.EnableManager;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.Order;
import examples.springdata.geode.domain.Product;
import examples.springdata.geode.util.LoggingCacheListener;

@EnableLocator
@EnableManager(start = true)
@EnableHttpService
@CacheServerApplication(port = 0, logLevel = "error", useClusterConfiguration = true)
@EnableClusterConfiguration(useHttp = true)
public class ClusterDefinedRegionServerConfig {

	@Bean
	LoggingCacheListener loggingCacheListener() {
		return new LoggingCacheListener();
	}

	@Bean
	ReplicatedRegionFactoryBean<Long, Customer> createCustomerRegion(GemFireCache gemfireCache) {
		ReplicatedRegionFactoryBean replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean();
		replicatedRegionFactoryBean.setCache(gemfireCache);
		replicatedRegionFactoryBean.setRegionName("Customers");
		replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
		replicatedRegionFactoryBean.setScope(Scope.DISTRIBUTED_ACK);
		replicatedRegionFactoryBean.setCacheListeners(new CacheListener[] { loggingCacheListener() });
		return replicatedRegionFactoryBean;
	}

	@Bean
	ReplicatedRegionFactoryBean<Long, Order> createOrderRegion(GemFireCache gemfireCache) {
		ReplicatedRegionFactoryBean replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean();
		replicatedRegionFactoryBean.setCache(gemfireCache);
		replicatedRegionFactoryBean.setRegionName("Orders");
		replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
		replicatedRegionFactoryBean.setScope(Scope.DISTRIBUTED_ACK);
		return replicatedRegionFactoryBean;
	}

	@Bean
	ReplicatedRegionFactoryBean<Long, Product> createProductRegion(GemFireCache gemfireCache) {
		ReplicatedRegionFactoryBean replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean();
		replicatedRegionFactoryBean.setCache(gemfireCache);
		replicatedRegionFactoryBean.setRegionName("Products");
		replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
		replicatedRegionFactoryBean.setScope(Scope.DISTRIBUTED_ACK);
		return replicatedRegionFactoryBean;
	}
}
