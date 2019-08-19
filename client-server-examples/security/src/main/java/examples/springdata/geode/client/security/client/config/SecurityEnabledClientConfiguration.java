package examples.springdata.geode.client.security.client.config;

import examples.springdata.geode.client.common.client.config.ClientApplicationConfig;
import examples.springdata.geode.client.security.client.repo.CustomerRepository;
import examples.springdata.geode.client.security.client.services.CustomerService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.gemfire.config.annotation.EnableSecurity;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@Configuration
@EnableSecurity
@Import(ClientApplicationConfig.class)
@ComponentScan(basePackageClasses = CustomerService.class)
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
public class SecurityEnabledClientConfiguration {
}
