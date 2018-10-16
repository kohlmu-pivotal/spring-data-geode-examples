package examples.springdata.geode.expiration.entity.config;

import examples.springdata.geode.expiration.entity.domain.Customer;
import examples.springdata.geode.expiration.entity.repo.CustomerRepository;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Scope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableExpiration;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@PeerCacheApplication
@EnableLocator
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@EnableExpiration
public class EntityDefinedExpirationServerConfig {

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
}
