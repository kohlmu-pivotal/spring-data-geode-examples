package examples.springdata.geode.server.expiration.custom.config;

import com.github.javafaker.Faker;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.server.expiration.custom.expiration.CustomCustomerExpiry;
import examples.springdata.geode.server.expiration.custom.repo.CustomerRepository;
import org.apache.geode.cache.CustomExpiry;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Scope;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@PeerCacheApplication(logLevel = "error")
@EnableLocator
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
public class CustomExpiryServerConfig {

    @Bean
    public Faker createDataFaker() {
        return new Faker();
    }

    @Bean("IDLE")
    CustomExpiry<Long, Customer> createIdleExpiration() {
        return new CustomCustomerExpiry(2);
    }

    @Bean("TTL")
    CustomExpiry<Long, Customer> createTtlExpiration() {
        return new CustomCustomerExpiry(4);
    }

    @Bean("Customers")
    public ReplicatedRegionFactoryBean<Long, Customer> createCustomerRegion(GemFireCache gemFireCache,
                                                                            @Qualifier("IDLE") CustomExpiry idleExpiry,
                                                                            @Qualifier("TTL") CustomExpiry ttlExpiry) {
        final ReplicatedRegionFactoryBean<Long, Customer> regionFactoryBean = new ReplicatedRegionFactoryBean<>();
        regionFactoryBean.setCache(gemFireCache);
        regionFactoryBean.setScope(Scope.DISTRIBUTED_ACK);
        regionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        regionFactoryBean.setName("Customers");
        regionFactoryBean.setStatisticsEnabled(true);
        regionFactoryBean.setCustomEntryIdleTimeout(idleExpiry);
        regionFactoryBean.setCustomEntryTimeToLive(ttlExpiry);
        return regionFactoryBean;
    }
}