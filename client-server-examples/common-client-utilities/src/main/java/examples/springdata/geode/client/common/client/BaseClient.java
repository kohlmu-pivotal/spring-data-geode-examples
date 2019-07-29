package examples.springdata.geode.client.common.client;

import examples.springdata.geode.client.common.client.service.CustomerService;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.domain.EmailAddress;

public interface BaseClient {

	default void populateData(CustomerService customerService)
	{
		System.out.println("Inserting 3 entries for keys: 1, 2, 3");
		customerService.save(new Customer(1L, new EmailAddress("2@2.com"), "John", "Smith"));
		customerService.save(new Customer(2L, new EmailAddress("3@3.com"), "Frank", "Lamport"));
		customerService.save(new Customer(3L, new EmailAddress("5@5.com"), "Jude", "Simmons"));

		System.out.println("Entries on Client: " + customerService.numberEntriesStoredLocally());
		System.out.println("Entries on Server: " + customerService.numberEntriesStoredOnServer());
		customerService.findAll().forEach(customer -> System.out.println("\t Entry: \n \t\t " + customer));

		System.out.println("Updating entry for key: 2");
		System.out.println("Entry Before: " + customerService.findById(2L).get());
		customerService.save(new Customer(2L, new EmailAddress("4@4.com"), "Sam", "Spacey"));
		System.out.println("Entry After: " + customerService.findById(2L).get());

		System.out.println("Removing entry for key: 3");
		customerService.deleteById(3L);

		System.out.println("Entries:");
		customerService.findAll().forEach(customer -> System.out.println("\t Entry: \n \t\t " + customer));
	}
}
