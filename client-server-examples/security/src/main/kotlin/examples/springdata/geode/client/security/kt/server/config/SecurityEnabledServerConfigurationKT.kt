package examples.springdata.geode.client.security.kt.server.config

import examples.springdata.geode.client.security.kt.server.managers.SimpleSecurityManager
import examples.springdata.geode.client.security.kt.server.repo.JdbcSecurityRepository
import examples.springdata.geode.client.security.kt.server.repo.SecurityRepository
import examples.springdata.geode.domain.Customer
import examples.springdata.geode.util.LoggingCacheListener
import org.apache.geode.cache.CacheListener
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Scope
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.*
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import javax.sql.DataSource


@Configuration
@EnableLocator
@EnableIndexing
@EnableManager
@CacheServerApplication(port = 0, logLevel = "info")
class SecurityEnabledServerConfigurationKT {
    @Bean
    internal fun loggingCacheListener() = LoggingCacheListener<Any, Any>()

    @Bean
    protected fun customerRegion(gemfireCache: GemFireCache) =
            ReplicatedRegionFactoryBean<Long, Customer>().apply {
                cache = gemfireCache
                setRegionName("Customers")
                scope = Scope.DISTRIBUTED_ACK
                dataPolicy = DataPolicy.REPLICATE
                setCacheListeners(arrayOf(loggingCacheListener() as CacheListener<Long, Customer>))
            }
}

@Configuration
@EnableSecurity(shiroIniResourcePath = "shiro.ini")
@Profile("shiro-ini-configuration")
class ApacheShiroIniConfigurationKT

@Configuration
@EnableSecurity(securityManagerClassName = "examples.springdata.geode.client.security.server.managers.SecurityManagerProxy")
@Profile("default", "geode-security-manager-proxy-configuration")
internal class GeodeIntegratedSecurityProxyConfigurationKT {
    @Bean
    fun hsqlDataSource(): DataSource {
        return with(EmbeddedDatabaseBuilder()) {
            setName("geode_security")
            setScriptEncoding("UTF-8")
            setType(EmbeddedDatabaseType.HSQL)
            addScript("sql/geode-security-schema-ddl.sql")
            addScript("sql/define-roles-table-ddl.sql")
            addScript("sql/define-roles-permissions-table-ddl.sql")
            addScript("sql/define-users-table-ddl.sql")
            addScript("sql/define-users-roles-table-ddl.sql")
            addScript("sql/insert-roles-dml.sql")
            addScript("sql/insert-roles-permissions-dml.sql")
            addScript("sql/insert-users-dml.sql")
            addScript("sql/insert-users-roles-dml.sql")
        }.build()
    }

    @Bean
    fun hsqlTemplate(hsqlDataSource: DataSource): JdbcTemplate = JdbcTemplate(hsqlDataSource)

    @Bean
    fun securityRepository(hsqlTemplate: JdbcTemplate): JdbcSecurityRepository =
            JdbcSecurityRepository(hsqlTemplate)

    @Bean
    fun securityManager(securityRepository: SecurityRepository): SimpleSecurityManager =
            SimpleSecurityManager(securityRepository)
}