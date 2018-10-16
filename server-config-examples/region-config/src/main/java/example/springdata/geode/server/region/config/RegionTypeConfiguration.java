package example.springdata.geode.server.region.config;

import example.springdata.geode.server.region.repo.CustomerRepository;
import example.springdata.geode.server.region.repo.OrderRepository;
import example.springdata.geode.server.region.repo.ProductRepository;
import examples.springdata.geode.domain.*;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.PartitionAttributes;
import org.apache.geode.cache.RegionAttributes;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.PartitionAttributesFactoryBean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import java.math.BigDecimal;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Configuration
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
public class RegionTypeConfiguration {

    @Bean
    RegionAttributesFactoryBean regionAttributes(PartitionAttributes partitionAttributes) {
        final RegionAttributesFactoryBean regionAttributesFactoryBean = new RegionAttributesFactoryBean();
        regionAttributesFactoryBean.setPartitionAttributes(partitionAttributes);
        return regionAttributesFactoryBean;
    }

    @Bean
    PartitionAttributesFactoryBean partitionAttributes(GemFireCache gemFireCache) {
        final PartitionAttributesFactoryBean<Long, Order> partitionAttributesFactoryBean = new PartitionAttributesFactoryBean<>();
        partitionAttributesFactoryBean.setTotalNumBuckets(13);
        partitionAttributesFactoryBean.setRedundantCopies(1);
        return partitionAttributesFactoryBean;
    }

    @Bean
    ReplicatedRegionFactoryBean createOrderRegion(GemFireCache gemFireCache) {
        final ReplicatedRegionFactoryBean<Long, Order> replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean<>();
        replicatedRegionFactoryBean.setCache(gemFireCache);
        replicatedRegionFactoryBean.setRegionName("Orders");
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        return replicatedRegionFactoryBean;
    }

    @Bean
    ReplicatedRegionFactoryBean createProductRegion(GemFireCache gemFireCache) {
        final ReplicatedRegionFactoryBean<Long, Product> replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean<>();
        replicatedRegionFactoryBean.setCache(gemFireCache);
        replicatedRegionFactoryBean.setRegionName("Products");
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        return replicatedRegionFactoryBean;
    }

    @Bean
    PartitionedRegionFactoryBean createCustomerRegion(GemFireCache gemFireCache, RegionAttributes<Long, Customer> regionAttributes) {
        final PartitionedRegionFactoryBean<Long, Customer> partitionedRegionFactoryBean = new PartitionedRegionFactoryBean<Long, Customer>();
        partitionedRegionFactoryBean.setCache(gemFireCache);
        partitionedRegionFactoryBean.setRegionName("Customers");
        partitionedRegionFactoryBean.setDataPolicy(DataPolicy.PARTITION);
        partitionedRegionFactoryBean.setAttributes(regionAttributes);
        return partitionedRegionFactoryBean;
    }

    @Profile({"default", "embeddedLocator"})
    @Configuration
    @EnableLocator
    @PeerCacheApplication(name = "embeddedLocator")
    static class EmbeddedLocator {
        @Bean
        public ApplicationRunner runner() {
            return args -> new Scanner(System.in).nextLine();
        }
    }

    @Profile("peer")
    @Configuration
    @PeerCacheApplication(locators = "localhost[10334]", name = "dataPeer")
    static class PeerServer {
        @Bean
        public ApplicationRunner runner() {
            return args -> new Scanner(System.in).nextLine();
        }
    }

    @Profile("dataPopulationPeer")
    @Configuration
    @PeerCacheApplication(locators = "localhost[10334]", name = "dataPopulationPeer")
    static class DataPopulationPeerServerKT {
        @Bean
        public ApplicationRunner runner(CustomerRepository customerRepository, OrderRepository orderRepository,
                                        ProductRepository productRepository) {
            return args -> {
                createCustomerData(customerRepository);

                createProducts(productRepository);

                createOrders(productRepository, orderRepository);

                new Scanner(System.in).nextLine();

            };
        }

        private void createOrders(ProductRepository productRepository, OrderRepository orderRepository) {
            Random random = new Random(System.nanoTime());
            Address address = new Address("it", "doesn't", "matter");
            LongStream.rangeClosed(1, 100).forEach((orderId) ->
                    LongStream.rangeClosed(1, 3000).forEach((customerId) -> {
                        Order order = new Order(orderId, customerId, address);
                        IntStream.rangeClosed(0, random.nextInt(3) + 1).forEach((lineItemCount) -> {
                            int quantity = random.nextInt(3) + 1;
                            long productId = random.nextInt(3) + 1;
                            order.add(new LineItem(productRepository.findById(productId).get(), quantity));
                        });
                        orderRepository.save(order);
                    }));
        }

        private void createProducts(ProductRepository productRepository) {
            productRepository.save(new Product(1L, "Apple iPod", new BigDecimal("99.99"),
                    "An Apple portable music player"));
            productRepository.save(new Product(2L, "Apple iPad", new BigDecimal("499.99"),
                    "An Apple tablet device"));
            Product
                    macbook =
                    new Product(3L, "Apple macBook", new BigDecimal("899.99"),
                            "An Apple notebook computer");
            macbook.addAttribute("warranty", "included");
            productRepository.save(macbook);
        }

        private void createCustomerData(CustomerRepository customerRepository) {
            System.out.println("Inserting 3 entries for keys: 1, 2, 3");
            LongStream.rangeClosed(0, 3000)
                    .parallel()
                    .forEach(customerId ->
                            customerRepository.save(new Customer(customerId, new EmailAddress(customerId + "@2.com"), "John" + customerId, "Smith" + customerId)));
        }
    }

}
