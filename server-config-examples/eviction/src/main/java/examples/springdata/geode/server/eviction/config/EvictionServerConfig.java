package examples.springdata.geode.server.eviction.config;

import com.github.javafaker.Faker;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.Order;
import examples.springdata.geode.domain.Product;
import examples.springdata.geode.server.eviction.repo.CustomerRepository;
import org.apache.geode.cache.*;
import org.apache.geode.cache.util.ObjectSizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.gemfire.DiskStoreFactoryBean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableEviction;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.gemfire.eviction.EvictionActionType;
import org.springframework.data.gemfire.eviction.EvictionPolicyType;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import java.io.File;
import java.util.Collections;

@PeerCacheApplication(criticalHeapPercentage = 0.7f, evictionHeapPercentage = 0.4f, logLevel = "error")
@EnableLocator
@EnableEviction(policies = @EnableEviction.EvictionPolicy(regionNames = "Orders",
        maximum = 10,
        action = EvictionActionType.LOCAL_DESTROY,
        type = EvictionPolicyType.ENTRY_COUNT))
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
public class EvictionServerConfig {
    @Bean
    public Faker faker() {
        return new Faker();
    }

    @Bean("DiskStoreDir")
    public File diskStoreDir(Faker faker) {
        final File file = new File("/tmp/" + faker.name().firstName());
        file.deleteOnExit();
        file.mkdirs();
        return file;
    }

    @Bean(name = "DiskStore")
    @DependsOn("DiskStoreDir")
    public DiskStoreFactoryBean diskStore(GemFireCache gemFireCache, @Qualifier("DiskStoreDir") File diskStoreDir) {
        final DiskStoreFactoryBean diskStoreFactoryBean = new DiskStoreFactoryBean();
        diskStoreFactoryBean.setDiskDirs(Collections.singletonList(new DiskStoreFactoryBean.DiskDir(diskStoreDir.getPath())));
        diskStoreFactoryBean.setCache(gemFireCache);
        return diskStoreFactoryBean;
    }

    @Bean("Orders")
    public ReplicatedRegionFactoryBean createOrderRegion(GemFireCache gemFireCache) {
        final ReplicatedRegionFactoryBean<Long, Order> replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean<>();
        replicatedRegionFactoryBean.setCache(gemFireCache);
        replicatedRegionFactoryBean.setScope(Scope.DISTRIBUTED_NO_ACK);
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        replicatedRegionFactoryBean.setName("Orders");
        replicatedRegionFactoryBean.setDiskStoreName("DiskStore");
        return replicatedRegionFactoryBean;
    }

    @Bean("Customers")
    public ReplicatedRegionFactoryBean createCustomerRegion(GemFireCache gemFireCache) {
        final ReplicatedRegionFactoryBean<Long, Customer> replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean<>();
        replicatedRegionFactoryBean.setCache(gemFireCache);
        replicatedRegionFactoryBean.setScope(Scope.DISTRIBUTED_NO_ACK);
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        replicatedRegionFactoryBean.setName("Customers");
        replicatedRegionFactoryBean.setDiskStoreName("DiskStore");
        replicatedRegionFactoryBean.setEvictionAttributes(
                EvictionAttributes.createLRUHeapAttributes(ObjectSizer.DEFAULT, EvictionAction.OVERFLOW_TO_DISK));
        return replicatedRegionFactoryBean;
    }

    @Bean("Products")
    public ReplicatedRegionFactoryBean createProductRegion(GemFireCache gemFireCache) {
        final ReplicatedRegionFactoryBean<Long, Product> replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean<>();
        replicatedRegionFactoryBean.setCache(gemFireCache);
        replicatedRegionFactoryBean.setScope(Scope.DISTRIBUTED_NO_ACK);
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        replicatedRegionFactoryBean.setName("Products");
        replicatedRegionFactoryBean.setDiskStoreName("DiskStore");
        replicatedRegionFactoryBean.setEvictionAttributes(
                EvictionAttributes.createLRUMemoryAttributes(10,
                        ObjectSizer.DEFAULT,
                        EvictionAction.OVERFLOW_TO_DISK));
        return replicatedRegionFactoryBean;
    }
}