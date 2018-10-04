package examples.springdata.geode.clusterregion.client.service;

import java.util.List;

import org.springframework.stereotype.Service;

import examples.springdata.geode.clusterregion.client.repo.ProductRepository;
import examples.springdata.geode.domain.Product;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId).get();
    }
}
