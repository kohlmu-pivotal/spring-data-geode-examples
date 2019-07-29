package examples.springdata.geode.client.serialization.kt.client.config

import examples.springdata.geode.client.serialization.kt.client.repo.CustomerRepositoryKT
import examples.springdata.geode.client.serialization.kt.client.services.CustomerServiceKT
import examples.springdata.geode.domain.Customer
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnablePdx
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@EnablePdx
@ComponentScan(basePackageClasses = [CustomerServiceKT::class])
@ClientCacheApplication(name = "PDXSerializedClient", logLevel = "error", pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
class PdxSerializationClientConfigKT {

    @Bean("Customers")
    protected fun configureProxyClientCustomerRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<Long, Customer>()
        .apply {
            cache = gemFireCache
            setName("Customers")
            setShortcut(ClientRegionShortcut.PROXY)
        }
}
