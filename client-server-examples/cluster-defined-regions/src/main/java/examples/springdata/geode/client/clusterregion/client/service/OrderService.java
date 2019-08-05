package examples.springdata.geode.client.clusterregion.client.service;

import java.util.List;

import examples.springdata.geode.client.clusterregion.client.repo.OrderRepository;
import org.springframework.stereotype.Service;

import examples.springdata.geode.domain.Order;

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
