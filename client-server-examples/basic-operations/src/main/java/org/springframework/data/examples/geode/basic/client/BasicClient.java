package org.springframework.data.examples.geode.basic.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.examples.geode.model.Customer;
import org.springframework.data.examples.geode.model.EmailAddress;

/**
 * Creates a client to demonstrate basic CRUD operations. This client can be configured in 2 ways, depending on profile
 * selected. "proxy" profile will create a region with PROXY configuration that will store no data locally. "localCache"
 * will create a region that stores data in the local client, to satisfy the "near cache" paradigm.
 *
 * @author Udo Kohlmeyer
 */
@SpringBootApplication(scanBasePackageClasses = BasicClientApplicationConfig.class)
public class BasicClient {

	private final CustomerService customerService;

	public BasicClient(CustomerService customerService) {
		this.customerService = customerService;
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(BasicClient.class, args);
		BasicClient client = applicationContext.getBean(BasicClient.class);

		System.out.println("Inserting 3 entries for keys: 1, 2, 3");
		CustomerService customerService = client.customerService;
		customerService.save(new Customer(1L, new EmailAddress("2@2.com"), "Me", "My"));
		customerService.save(new Customer(2L, new EmailAddress("3@3.com"), "You", "Yours"));
		customerService.save(new Customer(3L, new EmailAddress("5@5.com"), "Third", "Entry"));

		System.out.println("Entries on Client: " + client.customerService.numberEntriesStoredLocally());
		System.out.println("Entries on Server: " + client.customerService.numberEntriesStoredOnServer());
		customerService.findAll().forEach(customer -> System.out.println("\t Entry: \n \t\t " + customer));

		System.out.println("Updating entry for key: 2");
		System.out.println("Entry Before: " + customerService.findById(2L).get());
		customerService.save(new Customer(2L, new EmailAddress("4@4.com"), "First", "Update"));
		System.out.println("Entry After: " + customerService.findById(2L).get());

		System.out.println("Removing entry for key: 3");
		customerService.deleteById(3L);

		System.out.println("Entries:");
		customerService.findAll().forEach(customer -> System.out.println("\t Entry: \n \t\t " + customer));
	}
}