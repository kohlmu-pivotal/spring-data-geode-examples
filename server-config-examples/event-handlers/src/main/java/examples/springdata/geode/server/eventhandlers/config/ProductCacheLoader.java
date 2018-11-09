package examples.springdata.geode.server.eventhandlers.config;

import com.github.javafaker.Faker;
import examples.springdata.geode.domain.Product;
import org.apache.geode.cache.CacheLoader;
import org.apache.geode.cache.CacheLoaderException;
import org.apache.geode.cache.LoaderHelper;

import java.math.BigDecimal;

public class ProductCacheLoader implements CacheLoader<Long, Product> {
    private Faker faker = new Faker();

    @Override
    public Product load(LoaderHelper loaderHelper) throws CacheLoaderException {
        return new Product((long) loaderHelper.getKey(), randomStringName(), randomPrice());
    }

    private BigDecimal randomPrice() {
        return new BigDecimal(faker.commerce().price());
    }

    private String randomStringName() {
        return faker.commerce().productName();
    }
}
