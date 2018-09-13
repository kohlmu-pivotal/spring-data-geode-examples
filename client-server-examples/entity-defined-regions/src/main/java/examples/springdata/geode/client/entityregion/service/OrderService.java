package examples.springdata.geode.client.entityregion.service;

import examples.springdata.geode.client.entityregion.repo.OrderRepository;
import examples.springdata.geode.domain.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void save(Order order) {
        orderRepository.save(order);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).get();
    }
}
