package example.springdata.geode.server.kt.region.repo

import examples.springdata.geode.domain.Customer
import examples.springdata.geode.domain.Order
import examples.springdata.geode.domain.Product
import org.springframework.data.gemfire.mapping.annotation.Region
import org.springframework.data.repository.CrudRepository

@Region("Customers")
interface CustomerRepositoryKT : CrudRepository<Customer, Long>

@Region("Orders")
interface OrderRepositoryKT : CrudRepository<Order, Long>

@Region("Products")
interface ProductRepositoryKT : CrudRepository<Product, Long>