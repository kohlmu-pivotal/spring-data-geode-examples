package examples.springdata.geode.server.lucene.kt

import examples.springdata.geode.server.lucene.kt.domain.CustomerKT
import examples.springdata.geode.server.lucene.kt.domain.createLuceneCustomers
import examples.springdata.geode.server.lucene.kt.repo.CustomerRepositoryKT
import org.apache.geode.cache.lucene.LuceneService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.gemfire.search.lucene.LuceneTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [LuceneIndexServerKT::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class LuceneIndexServerKTTest {

    @Autowired
    lateinit var luceneTemplate: LuceneTemplate

    @Autowired
    lateinit var luceneService: LuceneService

    @Autowired
    lateinit var customerRepository: CustomerRepositoryKT

    @Test
    fun luceneIsConfiguredCorrectly() {
        assertThat(luceneService.allIndexes.size).isEqualTo(1)
        assertThat(luceneService.allIndexes.iterator().next().regionPath).isEqualTo("/Customers")
        assertThat(luceneService.allIndexes.iterator().next().fieldNames.size).isEqualTo(1)
        assertThat(luceneService.allIndexes.iterator().next().fieldNames[0]).isEqualTo("lastName")

        println("Inserting 300 customers")
        createLuceneCustomers(300, customerRepository)

        assertThat(customerRepository.count()).isEqualTo(300)

        println("Completed creating customers ")

        val lastName = luceneTemplate.query<Any, CustomerKT>("D*", "lastName", 300)

        println("Customers with last names beginning with 'D':")
        lastName.forEach { result -> println(result.value.toString()) }
    }
}