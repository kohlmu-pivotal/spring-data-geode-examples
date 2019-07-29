package examples.springdata.geode.client.security.kt.client.config

import examples.springdata.geode.client.security.kt.client.repo.CustomerRepositoryKT
import examples.springdata.geode.domain.Customer
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientRegionShortcut
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableSecurity
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@Configuration
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@EnableSecurity
@ClientCacheApplication(name = "SecurityClient", logLevel = "error", pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
class SecurityEnabledClientConfigurationKT {
    @Bean("Customers")
    protected fun configureProxyClientCustomerRegion(gemFireCache: GemFireCache) = ClientRegionFactoryBean<Long, Customer>()
            .apply {
                cache = gemFireCache
                setName("Customers")
                setShortcut(ClientRegionShortcut.PROXY)
            }
}