package org.springframework.data.examples.geode.function.client.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.examples.geode.function.client.functions.ProductFunctionExecutions;
import org.springframework.data.examples.geode.function.client.repo.ProductRepository;
import org.springframework.data.examples.geode.model.Product;
import org.springframework.stereotype.Service;

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
