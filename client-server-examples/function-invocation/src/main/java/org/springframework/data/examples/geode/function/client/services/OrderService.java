package org.springframework.data.examples.geode.function.client.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.examples.geode.function.client.functions.OrderFunctionExecutions;
import org.springframework.data.examples.geode.function.client.repo.OrderRepository;
import org.springframework.data.examples.geode.model.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderFunctionExecutions orderFunctionExecutions;

	public OrderService(OrderRepository orderRepository, OrderFunctionExecutions orderFunctionExecutions) {
		this.orderRepository = orderRepository;
		this.orderFunctionExecutions = orderFunctionExecutions;
	}

	public void save(Order order) {
		orderRepository.save(order);
	}

	public List<BigDecimal> sumPricesForAllProductsForOrder(Long orderId) {
		return orderFunctionExecutions.sumPricesForAllProductsForOrder(orderId);
	}

	public Order findById(Long orderId) {
		return orderRepository.findById(orderId).get();
	}
}
