package examples.springdata.geode.client.function.server.config;

import examples.springdata.geode.client.common.server.config.ServerApplicationConfig;
import examples.springdata.geode.client.function.client.repo.CustomerRepository;
import examples.springdata.geode.domain.Order;
import examples.springdata.geode.domain.Product;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctions;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@Configuration
@Import(ServerApplicationConfig.class)
@EnableGemfireFunctions
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
public class FunctionServerApplicationConfig {

	@Bean
	ReplicatedRegionFactoryBean<Long, Order> createOrderRegion(GemFireCache gemfireCache) {
		ReplicatedRegionFactoryBean replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean();
		replicatedRegionFactoryBean.setCache(gemfireCache);
		replicatedRegionFactoryBean.setRegionName("Orders");
		replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
		return replicatedRegionFactoryBean;
	}

	@Bean
	ReplicatedRegionFactoryBean<Long, Product> createProductRegion(GemFireCache gemfireCache) {
		ReplicatedRegionFactoryBean replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean();
		replicatedRegionFactoryBean.setCache(gemfireCache);
		replicatedRegionFactoryBean.setRegionName("Products");
		replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
		return replicatedRegionFactoryBean;
	}
}
