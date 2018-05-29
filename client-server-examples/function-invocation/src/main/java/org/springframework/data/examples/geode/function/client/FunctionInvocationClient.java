package org.springframework.data.examples.geode.function.client;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.examples.geode.function.client.services.CustomerService;
import org.springframework.data.examples.geode.function.client.services.OrderService;
import org.springframework.data.examples.geode.function.client.services.ProductService;
import org.springframework.data.examples.geode.model.Address;
import org.springframework.data.examples.geode.model.Customer;
import org.springframework.data.examples.geode.model.EmailAddress;
import org.springframework.data.examples.geode.model.LineItem;
import org.springframework.data.examples.geode.model.Order;
import org.springframework.data.examples.geode.model.Product;

/**
 * Creates a client to demonstrate OQL queries. This example will run queries against that local client data set and
 * again the remote servers. Depending on profile selected, the local query will either not return results (profile=proxy)
 * or it will return the same results as the remote query (profile=localCache)
 *
 * @author Udo Kohlmeyer
 */
@SpringBootApplication(scanBasePackageClasses = FunctionInvocationClient.class)
public class FunctionInvocationClient {

	private final CustomerService customerService;
	private final OrderService orderService;
	private final ProductService productService;

	public FunctionInvocationClient(CustomerService customerService, OrderService orderService,
		ProductService productService) {
		this.customerService = customerService;
		this.orderService = orderService;
		this.productService = productService;
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(FunctionInvocationClient.class, args);
		FunctionInvocationClient client = applicationContext.getBean(FunctionInvocationClient.class);

		CustomerService customerService = client.customerService;
		client.createCustomerData(customerService);

		System.out.println("All customers for emailAddresses:3@3.com,2@2.com using function invocation: \n\t "
			+ customerService.listAllCustomersForEmailAddress("2@2.com", "3@3.com"));

		ProductService productService = client.productService;
		client.createProducts(productService);
		System.out.println(
			"Running function to sum up all product prices: \n\t" + productService.sumPricesForAllProducts().get(0));

		OrderService orderService = client.orderService;
		client.createOrders(orderService);
		System.out.println(
			"Running function to sum up all order lineItems prices for order 1: \n\t"
				+ orderService.sumPricesForAllProductsForOrder(1L).get(0));
		System.out.println("For order: \n\t " + orderService.findById(1L));
	}

	private void createOrders(OrderService orderService) {
		Random random = new Random(new Date().getTime());
		Address address = new Address("it", "doesn't", "matter");
		LongStream.rangeClosed(1, 100).forEach((orderId) ->
			LongStream.rangeClosed(1, 3).forEach((customerId) -> {
				Order order = new Order(orderId, customerId, address);
				IntStream.rangeClosed(0, random.nextInt(30) + 1).forEach((lineItemCount) -> {
					int quantity = random.nextInt(3) + 1;
					long productId = random.nextInt(3) + 1;
					order.add(new LineItem(productService.findById(productId), quantity));
				});
				orderService.save(order);
			}));
	}

	private void createProducts(ProductService productService) {
		productService.save(new Product(1L, "Apple iPod", new BigDecimal(99.99), "An Apple portable music player"));
		productService.save(new Product(2L, "Apple iPad", new BigDecimal(499.99), "An Apple tablet device"));
		Product macbook = new Product(3L, "Apple macBook", new BigDecimal(899.99), "An Apple notebook computer");
		macbook.addAttribute("warranty", "included");
		productService.save(macbook);
	}

	private void createCustomerData(CustomerService customerService) {
		System.out.println("Inserting 3 entries for keys: 1, 2, 3");
		customerService.save(new Customer(1L, new EmailAddress("2@2.com"), "John", "Smith"));
		customerService.save(new Customer(2L, new EmailAddress("3@3.com"), "Frank", "Lamport"));
		customerService.save(new Customer(3L, new EmailAddress("5@5.com"), "Jude", "Simmons"));
	}
}