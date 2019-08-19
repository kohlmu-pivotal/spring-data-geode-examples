package examples.springdata.geode.client.security.client;

import examples.springdata.geode.client.security.client.services.CustomerService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import examples.springdata.geode.client.security.client.config.SecurityEnabledClientConfiguration;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;

@SpringBootApplication(scanBasePackageClasses = SecurityEnabledClientConfiguration.class)
public class SecurityEnabledClient {

	@Bean
	ApplicationRunner runner(CustomerService customerService) {
		return args -> {
			customerService.save(new Customer(1L, new EmailAddress("2@2.com"), "John", "Smith"));
			System.out.println("Customers saved on server:");
			customerService.findAll().forEach(customer -> System.out.println("\t Entry: \n \t\t " + customer));
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(SecurityEnabledClient.class, args);
	}
}