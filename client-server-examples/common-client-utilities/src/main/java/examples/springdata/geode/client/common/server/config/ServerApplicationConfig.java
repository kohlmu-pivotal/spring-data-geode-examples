package examples.springdata.geode.client.common.server.config;

import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.util.LoggingCacheListener;
import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Scope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.EnableIndexing;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.EnableManager;

@EnableLocator
@EnableIndexing
@EnableManager
@CacheServerApplication(port = 0)
public class ServerApplicationConfig {

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
        replicatedRegionFactoryBean.setCacheListeners(new CacheListener[]{loggingCacheListener()});
        return replicatedRegionFactoryBean;
    }

}
