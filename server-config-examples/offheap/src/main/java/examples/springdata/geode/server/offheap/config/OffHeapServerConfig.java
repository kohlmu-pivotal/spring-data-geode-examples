package examples.springdata.geode.server.offheap.config;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.Product;
import examples.springdata.geode.server.offheap.repo.CustomerRepository;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Scope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.EnableOffHeap;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@PeerCacheApplication
@EnableLocator
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@EnableOffHeap(memorySize = "8192m", regionNames = {"Customers"})
public class OffHeapServerConfig {

    @Bean("Customers")
    public ReplicatedRegionFactoryBean<Long, Customer> createCustomerRegion(GemFireCache gemFireCache) {
        final ReplicatedRegionFactoryBean<Long, Customer> regionFactoryBean = new ReplicatedRegionFactoryBean<>();
        regionFactoryBean.setCache(gemFireCache);
        regionFactoryBean.setScope(Scope.DISTRIBUTED_ACK);
        regionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        regionFactoryBean.setName("Customers");
        regionFactoryBean.setStatisticsEnabled(true);
        return regionFactoryBean;
    }

    @Bean("Products")
    public ReplicatedRegionFactoryBean<Long, Product> createProductRegion(GemFireCache gemFireCache) {
        final ReplicatedRegionFactoryBean<Long, Product> regionFactoryBean = new ReplicatedRegionFactoryBean<>();
        regionFactoryBean.setCache(gemFireCache);
        regionFactoryBean.setScope(Scope.DISTRIBUTED_ACK);
        regionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        regionFactoryBean.setName("Products");
        regionFactoryBean.setStatisticsEnabled(true);
        regionFactoryBean.setOffHeap(true);
        return regionFactoryBean;
    }
}
