package org.springframework.data.examples.geode.function.client.functions;

import java.util.List;

import org.springframework.data.examples.geode.model.Customer;
import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnRegion;

@OnRegion(region = "Customers")
public interface CustomerFunctionExecutions {

	@FunctionId("listConsumersForEmailAddressesFnc")
	List<Customer> listAllCustomersForEmailAddress(String... emailAddresses);
}