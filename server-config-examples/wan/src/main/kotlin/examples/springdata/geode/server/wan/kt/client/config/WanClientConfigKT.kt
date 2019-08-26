package examples.springdata.geode.server.wan.kt.client.config

import examples.springdata.geode.client.common.kt.client.config.ClientApplicationConfigKT
import examples.springdata.geode.server.wan.kt.client.repo.CustomerRepositoryKT
import examples.springdata.geode.server.wan.kt.client.services.CustomerServiceKT
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@Configuration
@Import(ClientApplicationConfigKT::class)
@ComponentScan(basePackageClasses = [CustomerServiceKT::class])
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
class WanClientConfigKT

