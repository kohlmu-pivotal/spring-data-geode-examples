package examples.springdata.geode.client.function.server.functions;

import examples.springdata.geode.domain.Customer;
import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.data.gemfire.function.annotation.RegionData;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CustomerFunctions {

	@GemfireFunction(id = "listConsumersForEmailAddressesFnc", HA = true, optimizeForWrite = true, batchSize = 3, hasResult = true)
	public List<Customer> listAllCustomersForEmailAddress(@RegionData Map<Long, Customer> customerData,
		String... emailAddresses) {
		List<String> emailAddressesAsList = Arrays.asList(emailAddresses);
		List<Customer> collect = customerData.values().parallelStream()
			.filter((customer) -> emailAddressesAsList.contains(customer.getEmailAddress().getValue()))
			.collect(Collectors.toList());
		return collect;
	}
}
