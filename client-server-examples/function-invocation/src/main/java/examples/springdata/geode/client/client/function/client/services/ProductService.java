package examples.springdata.geode.client.client.function.client.services;

import examples.springdata.geode.client.client.function.client.repo.ProductRepository;
import examples.springdata.geode.client.client.function.client.functions.ProductFunctionExecutions;
import examples.springdata.geode.client.client.function.client.repo.ProductRepository;
import examples.springdata.geode.domain.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductFunctionExecutions productFunctionExecutions;

	public ProductService(ProductRepository productRepository, ProductFunctionExecutions productFunctionExecutions) {
		this.productRepository = productRepository;
		this.productFunctionExecutions = productFunctionExecutions;
	}

	public void save(Product product) {
		productRepository.save(product);
	}

	public List<BigDecimal> sumPricesForAllProducts() {
		return productFunctionExecutions.sumPricesForAllProducts();
	}

	public Product findById(Long productId) {
		return productRepository.findById(productId).get();
	}
}
