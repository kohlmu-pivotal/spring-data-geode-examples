package examples.springdata.geode.server.eviction.kt.config

import com.github.javafaker.Faker
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.Order
import examples.springdata.geode.domain.Product
import examples.springdata.geode.server.eviction.kt.repo.CustomerRepositoryKT
import org.apache.geode.cache.*
import org.apache.geode.cache.util.ObjectSizer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import org.springframework.data.gemfire.DiskStoreFactoryBean
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.EnableEviction
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import org.springframework.data.gemfire.eviction.EvictionActionType
import org.springframework.data.gemfire.eviction.EvictionPolicyType
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import java.io.File

@PeerCacheApplication(criticalHeapPercentage = 0.7f, evictionHeapPercentage = 0.4f, logLevel = "error")
@EnableLocator
@EnableEviction(policies = [EnableEviction.EvictionPolicy(regionNames = ["Orders"],
        maximum = 10,
        action = EvictionActionType.LOCAL_DESTROY,
        type = EvictionPolicyType.ENTRY_COUNT)])
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
class EvictionServerConfigKT {
    @Bean
    fun faker() = Faker()

    @Bean("DiskStoreDir")
    fun diskStoreDir(faker: Faker): File =
            File("/tmp/" + faker.name().firstName()).apply {
                deleteOnExit()
                mkdirs()
            }

    @Bean(name = ["DiskStore"])
    @DependsOn("DiskStoreDir")
    fun diskStore(gemFireCache: GemFireCache, @Qualifier("DiskStoreDir") diskStoreDir: File): DiskStoreFactoryBean {
        return DiskStoreFactoryBean().apply {
            setDiskDirs(listOf(DiskStoreFactoryBean.DiskDir(diskStoreDir.path)))
            setCache(gemFireCache)
        }
    }

    @Bean("Orders")
    fun createOrderRegion(gemFireCache: GemFireCache) =
            ReplicatedRegionFactoryBean<Long, Order>().apply {
                cache = gemFireCache
                scope = Scope.DISTRIBUTED_NO_ACK
                dataPolicy = DataPolicy.REPLICATE
                setName("Orders")
                setDiskStoreName("DiskStore")
            }

    @Bean("Customers")
    fun createCustomerRegion(gemFireCache: GemFireCache) =
            ReplicatedRegionFactoryBean<Long, Customer>().apply {
                cache = gemFireCache
                scope = Scope.DISTRIBUTED_NO_ACK
                dataPolicy = DataPolicy.REPLICATE
                setName("Customers")
                setEvictionAttributes(EvictionAttributes.createLRUHeapAttributes(ObjectSizer.DEFAULT, EvictionAction.OVERFLOW_TO_DISK))
            }

    @Bean("Products")
    fun createProductRegion(gemFireCache: GemFireCache) =
            ReplicatedRegionFactoryBean<Long, Product>().apply {
                cache = gemFireCache
                scope = Scope.DISTRIBUTED_NO_ACK
                dataPolicy = DataPolicy.REPLICATE
                setName("Products")
                setDiskStoreName("DiskStore")
                setEvictionAttributes(EvictionAttributes.createLRUMemoryAttributes(10, ObjectSizer.DEFAULT, EvictionAction.OVERFLOW_TO_DISK))
            }
}