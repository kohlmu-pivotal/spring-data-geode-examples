package examples.springdata.geode.cq.client.producer.config;

import examples.springdata.geode.common.client.ClientApplicationConfig;
import examples.springdata.geode.cq.client.producer.repo.CustomerRepository;
import examples.springdata.geode.cq.client.producer.services.CustomerService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@Configuration
@Import(ClientApplicationConfig.class)
@ComponentScan(basePackageClasses = CustomerService.class)
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@ClientCacheApplication(name = "CQProducerClientCache", logLevel = "info", pingInterval = 5000L, readTimeout = 15000,
        retryAttempts = 1, subscriptionEnabled = false)
public class CQProducerClientApplicationConfig {

}
