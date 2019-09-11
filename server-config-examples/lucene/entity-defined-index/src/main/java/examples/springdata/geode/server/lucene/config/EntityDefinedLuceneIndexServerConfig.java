package examples.springdata.geode.server.lucene.config;

import examples.springdata.geode.server.lucene.domain.Customer;
import examples.springdata.geode.server.lucene.repo.CustomerRepository;
import org.apache.geode.cache.GemFireCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableIndexing;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.search.lucene.LuceneServiceFactoryBean;
import org.springframework.data.gemfire.search.lucene.LuceneTemplate;

@PeerCacheApplication(logLevel = "error")
@EnableLocator
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
@EnableEntityDefinedRegions(basePackageClasses = Customer.class)
@EnableIndexing(define = true)
public class EntityDefinedLuceneIndexServerConfig {

    @Bean
    @DependsOn("lastName_lucene")
    LuceneTemplate createCustomerLuceneTemplate() {
        return new LuceneTemplate("lastName_lucene", "/Customers");
    }

    @Bean
    LuceneServiceFactoryBean luceneService(GemFireCache gemfireCache) {
        LuceneServiceFactoryBean luceneService = new LuceneServiceFactoryBean();
        luceneService.setCache(gemfireCache);
        return luceneService;
    }
}
