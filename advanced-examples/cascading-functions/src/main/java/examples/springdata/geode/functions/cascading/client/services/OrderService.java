package examples.springdata.geode.functions.cascading.client.services;

import examples.springdata.geode.domain.Order;
import examples.springdata.geode.functions.cascading.client.functions.OrderFunctionExecutions;
import examples.springdata.geode.functions.cascading.client.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderFunctionExecutions orderFunctionExecutions;

    public void save(Order order) {
        orderRepository.save(order);
    }

    public List<Order> findOrdersForCustomers(List<Long> customerIds) {
        return orderFunctionExecutions.findOrdersForCustomers(customerIds);
    }
}
