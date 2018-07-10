package example.springdata.geode.client.security.server.config;

import example.springdata.geode.client.security.kt.domain.User;
import example.springdata.geode.client.security.kt.server.managers.SimpleSecurityManager;
import example.springdata.geode.client.security.kt.server.repo.JdbcSecurityRepository;
import example.springdata.geode.client.security.kt.server.repo.SecurityRepository;
import examples.springdata.geode.client.common.server.config.ServerApplicationConfig;
import org.apache.geode.internal.security.shiro.GeodePermissionResolver;
import org.apache.shiro.realm.text.PropertiesRealm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.config.annotation.EnableSecurity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@Import(ServerApplicationConfig.class)
public class SecurityEnabledServerConfiguration {

}

@Configuration
@EnableSecurity(shiroIniResourcePath = "shiro.ini")
@Profile("shiro-ini-configuration")
class ApacheShiroIniConfiguration {
}

@Configuration
@EnableSecurity
@Profile({"default", "shiro-properties-configuration"})
class ApacheShiroRealmConfiguration {
    @Bean
    public PropertiesRealm shiroRealm() {

        PropertiesRealm propertiesRealm = new PropertiesRealm();

        propertiesRealm.setResourcePath("classpath:server/shiro.properties");
        propertiesRealm.setPermissionResolver(new GeodePermissionResolver());

        return propertiesRealm;
    }
}

@Configuration
@EnableSecurity(securityManagerClassName = "example.app.geode.security.SecurityManagerProxy")
@Profile("geode-security-manager-proxy-configuration")
class GeodeIntegratedSecurityProxyConfiguration {

    @Bean
    DataSource hsqlDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setName("geode_security")
                .setScriptEncoding("UTF-8")
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("geode-security-schema-ddl.sql")
                .addScript("define-roles-table-ddl.sql")
                .addScript("define-roles-permissions-table-ddl.sql")
                .addScript("define-users-table-ddl.sql")
                .addScript("define-users-roles-table-ddl.sql")
                .addScript("insert-roles-dml.sql")
                .addScript("insert-roles-permissions-dml.sql")
                .addScript("insert-users-dml.sql")
                .addScript("insert-users-roles-dml.sql")
                .build();
    }

    @Bean
    JdbcTemplate hsqlTemplate(DataSource hsqlDataSource) {
        return new JdbcTemplate(hsqlDataSource);
    }

    @Bean
    JdbcSecurityRepository securityRepository(JdbcTemplate hsqlTemplate) {
        return new JdbcSecurityRepository(hsqlTemplate);
    }

    @Bean
    SimpleSecurityManager securityManager(SecurityRepository<User> securityRepository) {
        return new SimpleSecurityManager(securityRepository);
    }
}

