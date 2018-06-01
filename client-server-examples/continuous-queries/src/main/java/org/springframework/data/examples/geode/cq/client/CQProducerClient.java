package org.springframework.data.examples.geode.cq.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.examples.geode.cq.client.services.CustomerService;
import org.springframework.data.examples.geode.model.Customer;
import org.springframework.data.examples.geode.model.EmailAddress;

/**
 * Creates a client to demonstrate OQL queries. This example will run queries against that local client data set and
 * again the remote servers. Depending on profile selected, the local query will either not return results (profile=proxy)
 * or it will return the same results as the remote query (profile=localCache)
 *
 * @author Udo Kohlmeyer
 */
@SpringBootApplication(scanBasePackageClasses = CQClientApplicationConfig.class)
public class CQProducerClient {

	private final CustomerService customerService;

	public CQProducerClient(CustomerService customerService) {
		this.customerService = customerService;
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(CQProducerClient.class, args);
		CQProducerClient client = applicationContext.getBean(CQProducerClient.class);

		CustomerService customerService = client.customerService;
		System.out.println("Inserting 3 entries for keys: 1, 2, 3");
		customerService.save(new Customer(1L, new EmailAddress("2@2.com"), "John", "Smith"));
		customerService.save(new Customer(2L, new EmailAddress("3@3.com"), "Frank", "Lamport"));
		customerService.save(new Customer(3L, new EmailAddress("5@5.com"), "Jude", "Simmons"));


		System.out.println("Find customers with firstName=Jude on local client region: " +
			customerService.findByFirstNameLocalClientRegion("select * from /Customers where firstName=$1", "Jude"));
	}
}