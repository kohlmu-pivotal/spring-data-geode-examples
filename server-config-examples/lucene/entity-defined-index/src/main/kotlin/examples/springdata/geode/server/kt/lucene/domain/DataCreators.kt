package examples.springdata.geode.server.kt.lucene.domain

import com.github.javafaker.Faker
import examples.springdata.geode.domain.EmailAddress
import examples.springdata.geode.server.lucene.domain.JavaLuceneCustomer
import org.springframework.data.repository.CrudRepository

fun createLuceneCustomers(numberOfCustomer: Int, repository: CrudRepository<JavaLuceneCustomer, Long>) {
    val faker = Faker()
    val fakerName = faker.name()
    val fakerInternet = faker.internet()
    (0..numberOfCustomer).forEachIndexed { index, _ ->
        repository.save(JavaLuceneCustomer(index.toLong(),
                EmailAddress(fakerInternet.emailAddress()), fakerName.firstName(), fakerName.lastName()))
    }
}