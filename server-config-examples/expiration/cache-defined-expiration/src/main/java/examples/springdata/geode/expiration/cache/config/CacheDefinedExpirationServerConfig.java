package examples.springdata.geode.expiration.cache.config;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.expiration.cache.repo.CustomerRepository;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Scope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableExpiration;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.gemfire.expiration.ExpirationActionType;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import static org.springframework.data.gemfire.config.annotation.EnableExpiration.ExpirationPolicy;
import static org.springframework.data.gemfire.config.annotation.EnableExpiration.ExpirationType;

@PeerCacheApplication
@EnableLocator
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@EnableExpiration(policies = {
        @ExpirationPolicy(timeout = 4, action = ExpirationActionType.DESTROY,
                regionNames = {"Customers"}, types = {ExpirationType.TIME_TO_LIVE}),
        @ExpirationPolicy(timeout = 2, action = ExpirationActionType.DESTROY,
                regionNames = {"Customers"}, types = {ExpirationType.IDLE_TIMEOUT})})

public class CacheDefinedExpirationServerConfig {

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
