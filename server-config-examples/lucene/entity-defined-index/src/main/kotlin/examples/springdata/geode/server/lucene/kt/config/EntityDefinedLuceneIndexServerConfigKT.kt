package examples.springdata.geode.server.lucene.kt.config;

import examples.springdata.geode.server.lucene.kt.domain.CustomerKT
import examples.springdata.geode.server.lucene.kt.repo.CustomerRepositoryKT
import org.apache.geode.cache.GemFireCache
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions
import org.springframework.data.gemfire.config.annotation.EnableIndexing
import org.springframework.data.gemfire.config.annotation.EnableLocator
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.data.gemfire.search.lucene.LuceneServiceFactoryBean
import org.springframework.data.gemfire.search.lucene.LuceneTemplate

@PeerCacheApplication(logLevel = "error")
@EnableLocator
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@EnableEntityDefinedRegions(basePackageClasses = [CustomerKT::class])
@EnableIndexing(define = true)
class EntityDefinedLuceneIndexServerConfigKT {

    @Bean
    @DependsOn("lastName_lucene")
    internal fun createCustomerLuceneTemplate(): LuceneTemplate {
        return LuceneTemplate("lastName_lucene", "/Customers")
    }

    @Bean
    internal fun luceneService(gemfireCache: GemFireCache): LuceneServiceFactoryBean {
        val luceneService = LuceneServiceFactoryBean()
        luceneService.setCache(gemfireCache)
        return luceneService
    }
}