package examples.springdata.geode.client.basic.config;

import examples.springdata.geode.client.basic.repo.CustomerRepository;
import examples.springdata.geode.client.basic.services.CustomerService;
import examples.springdata.geode.client.common.client.config.ClientApplicationConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

/**
 * Spring JavaConfig configuration class to setup a Spring container and infrastructure components.
 *
 * @author Udo Kohlmeyer
 */
@Configuration
@Import(ClientApplicationConfig.class)
@ComponentScan(basePackageClasses = CustomerService.class)
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
public class BasicClientApplicationConfig {

}
