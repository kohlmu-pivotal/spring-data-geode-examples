package examples.springdata.geode.client.basic.config;

import examples.springdata.geode.client.basic.repo.CustomerRepository;
import examples.springdata.geode.client.basic.services.CustomerService;
import examples.springdata.geode.domain.Customer;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

/**
 * Spring JavaConfig configuration class to setup a Spring container and infrastructure components.
 *
 * @author Udo Kohlmeyer
 * @author Patrick Johnson
 */
@Configuration
@ComponentScan(basePackageClasses = CustomerService.class)
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@ClientCacheApplication(name = "BasicClient", logLevel = "warn", pingInterval = 5000L, readTimeout = 15000, retryAttempts = 1)
public class BasicClientApplicationConfig {
    @Bean("Customers")
    protected ClientRegionFactoryBean<Long, Customer> configureProxyClientCustomerRegion(GemFireCache gemFireCache) {
        ClientRegionFactoryBean<Long,Customer> clientRegionFactoryBean = new ClientRegionFactoryBean<>();
        clientRegionFactoryBean.setCache(gemFireCache);
        clientRegionFactoryBean.setName("Customers");
        clientRegionFactoryBean.setShortcut(ClientRegionShortcut.PROXY);
        return clientRegionFactoryBean;
    }
}