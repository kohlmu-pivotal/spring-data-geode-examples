package examples.springdata.geode.server.lucene.kt.domain

import com.github.javafaker.Faker
import examples.springdata.geode.domain.EmailAddress
import org.springframework.data.repository.CrudRepository

fun createLuceneCustomers(numberOfCustomer: Int, repository: CrudRepository<CustomerKT, Long>) {
    val faker = Faker()
    val fakerName = faker.name()
    val fakerInternet = faker.internet()
    (0 until numberOfCustomer).forEachIndexed { index, _ ->
        repository.save(CustomerKT(index.toLong(),
                EmailAddress(fakerInternet.emailAddress()), fakerName.firstName(), fakerName.lastName()))
    }
}