package examples.springdata.geode.function.client.services;

import examples.springdata.geode.domain.Order;
import examples.springdata.geode.function.client.functions.OrderFunctionExecutions;
import examples.springdata.geode.function.client.repo.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
