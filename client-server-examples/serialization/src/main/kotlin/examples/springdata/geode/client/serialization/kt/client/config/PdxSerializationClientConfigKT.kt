package examples.springdata.geode.client.serialization.kt.client.config

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.client.serialization.kt.client.repo.CustomerRepositoryKT
import examples.springdata.geode.client.serialization.kt.client.services.CustomerServiceKT
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer
import org.springframework.data.gemfire.config.annotation.EnablePdx
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.gemfire.support.ConnectionEndpoint

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

    @Bean
    internal fun clientCacheServerConfigurer(
        @Value("\${spring.data.geode.locator.host:localhost}") hostname: String,
        @Value("\${spring.data.geode.locator.port:10334}") port: Int) =
        ClientCacheConfigurer { _, clientCacheFactoryBean ->
            clientCacheFactoryBean.setLocators(listOf(ConnectionEndpoint(hostname, port)))
        }
}
