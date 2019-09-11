package examples.springdata.geode.server.compression.config;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.server.compression.repo.CustomerRepository;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.compression.Compressor;
import org.apache.geode.compression.SnappyCompressor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@PeerCacheApplication(logLevel = "error")
@EnableLocator
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
public class CompressionEnabledServerConfig {

    @Bean
    Compressor createSnappyCompressor() {
        return new SnappyCompressor();
    }

    @Bean("Customers")
    ReplicatedRegionFactoryBean createCustomerRegion(GemFireCache gemFireCache, Compressor compressor) {
        final ReplicatedRegionFactoryBean<Long, Customer> replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean<Long, Customer>();
        replicatedRegionFactoryBean.setCache(gemFireCache);
        replicatedRegionFactoryBean.setRegionName("Customers");
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        replicatedRegionFactoryBean.setCompressor(compressor);
        return replicatedRegionFactoryBean;
    }
}
