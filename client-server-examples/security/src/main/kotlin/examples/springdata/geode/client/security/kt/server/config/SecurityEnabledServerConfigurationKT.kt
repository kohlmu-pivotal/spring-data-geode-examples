package examples.springdata.geode.client.security.kt.server.config

import examples.springdata.geode.client.security.kt.server.managers.SimpleSecurityManager
import examples.springdata.geode.client.security.kt.server.repo.JdbcSecurityRepository
import examples.springdata.geode.client.security.kt.server.repo.SecurityRepository
import examples.springdata.geode.client.security.kt.server.repo.XmlSecurityRepository
import examples.springdata.geode.client.security.kt.shiro.realm.SecurityRepositoryAuthorizingRealm
import examples.springdata.geode.client.common.kt.server.config.ServerApplicationConfigKT
import org.apache.geode.internal.security.shiro.GeodePermissionResolver
import org.apache.shiro.realm.Realm
import org.apache.shiro.realm.text.PropertiesRealm
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.data.gemfire.config.annotation.EnableSecurity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import javax.sql.DataSource


@Configuration
@Import(ServerApplicationConfigKT::class)
class SecurityEnabledServerConfigurationKT

@Configuration
@EnableSecurity(shiroIniResourcePath = "shiro.ini")
@Profile("shiro-ini-configuration")
class ApacheShiroIniConfigurationKT

@Configuration
@EnableSecurity
@Profile("default", "shiro-ini-configuration")
class ApacheShiroRealmConfigurationKT {
    @Bean
    fun shiroRealm(): PropertiesRealm = PropertiesRealm().apply {
        setResourcePath("classpath:server/shiro.properties")
        permissionResolver = GeodePermissionResolver()
    }
}

@Configuration
@EnableSecurity
@Profile("shiro-custom-realm-configuration")
internal class ApacheShiroCustomRealmConfigurationKT {
    @Bean
    fun securityRepository(): SecurityRepository = XmlSecurityRepository()

    @Bean
    fun geodeRealm(securityRepository: SecurityRepository): Realm = SecurityRepositoryAuthorizingRealm(securityRepository)
}

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
