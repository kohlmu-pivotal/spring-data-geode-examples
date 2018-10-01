package example.springdata.geode.client.security.kt.client.config

import example.springdata.geode.client.security.kt.client.repo.CustomerRepositoryKT
import examples.springdata.geode.client.common.kt.client.config.ClientApplicationConfigKT
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.gemfire.config.annotation.EnableSecurity
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@Configuration
@Import(ClientApplicationConfigKT::class)
@EnableGemfireRepositories(basePackageClasses = [CustomerRepositoryKT::class])
@EnableSecurity
class SecurityEnabledClientConfigurationKT