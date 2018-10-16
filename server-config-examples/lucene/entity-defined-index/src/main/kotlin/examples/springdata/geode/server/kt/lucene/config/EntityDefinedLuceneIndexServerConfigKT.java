package examples.springdata.geode.server.kt.lucene.config;

import examples.springdata.geode.server.lucene.repo.LuceneCustomerRepo;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@PeerCacheApplication
@EnableLocator
@EnableGemfireRepositories(basePackageClasses = LuceneCustomerRepo.class)
public class EntityDefinedLuceneIndexServerConfigKT {
}
