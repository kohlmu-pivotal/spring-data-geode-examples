package example.springdata.geode.client.security.client;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import example.springdata.geode.client.security.client.config.SecurityEnabledClientConfiguration;
import example.springdata.geode.client.security.client.repo.CustomerRepository;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;

@SpringBootApplication(scanBasePackageClasses = SecurityEnabledClientConfiguration.class)
public class SecurityEnabledClient {

	@Bean
	ApplicationRunner runner(CustomerRepository customerRepository) {
		return args -> {
			customerRepository.save(
				new Customer(1L, new EmailAddress("2@2.com"), "John", "Smith"));
			System.out.println("Customers saved on server:");
			customerRepository.findAll().forEach(customer -> System.out.println("\t Entry: \n \t\t " + customer));
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(SecurityEnabledClient.class, args);
	}
}