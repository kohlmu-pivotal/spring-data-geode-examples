package example.springdata.geode.client.security.client.config;

import example.springdata.geode.client.security.client.repo.CustomerRepository;
import examples.springdata.geode.client.common.client.config.ClientApplicationConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.gemfire.config.annotation.EnableSecurity;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@Configuration
@EnableSecurity
@Import(ClientApplicationConfig.class)
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
public class SecurityEnabledClientConfiguration {
}
