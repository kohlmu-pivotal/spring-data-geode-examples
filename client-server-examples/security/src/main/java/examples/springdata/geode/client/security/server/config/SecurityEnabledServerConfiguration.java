package examples.springdata.geode.client.security.server.config;

import examples.springdata.geode.client.security.kt.server.managers.SimpleSecurityManager;
import examples.springdata.geode.client.security.kt.server.repo.JdbcSecurityRepository;
import examples.springdata.geode.client.security.kt.server.repo.SecurityRepository;
import examples.springdata.geode.domain.Customer;
import examples.springdata.geode.util.LoggingCacheListener;
import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Scope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@EnableLocator
@EnableIndexing
@EnableManager
@CacheServerApplication(port = 0, logLevel = "error")
public class SecurityEnabledServerConfiguration {
    @Bean
    LoggingCacheListener loggingCacheListener() {
        return new LoggingCacheListener();
    }

    @Bean
    ReplicatedRegionFactoryBean<Long, Customer> createCustomerRegion(GemFireCache gemfireCache) {
        ReplicatedRegionFactoryBean replicatedRegionFactoryBean = new ReplicatedRegionFactoryBean();
        replicatedRegionFactoryBean.setCache(gemfireCache);
        replicatedRegionFactoryBean.setRegionName("Customers");
        replicatedRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        replicatedRegionFactoryBean.setScope(Scope.DISTRIBUTED_ACK);
        replicatedRegionFactoryBean.setCacheListeners(new CacheListener[]{loggingCacheListener()});
        return replicatedRegionFactoryBean;
    }
}

@Configuration
@EnableSecurity(shiroIniResourcePath = "shiro.ini")
@Profile("shiro-ini-configuration")
class ApacheShiroIniConfiguration {
}

@Configuration
@EnableSecurity(securityManagerClassName = "examples.springdata.geode.client.security.server.managers.SecurityManagerProxy")
@Profile({"default", "geode-security-manager-proxy-configuration"})
class GeodeIntegratedSecurityProxyConfiguration {

    @Bean
    DataSource hsqlDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setName("geode_security")
                .setScriptEncoding("UTF-8")
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("sql/geode-security-schema-ddl.sql")
                .addScript("sql/define-roles-table-ddl.sql")
                .addScript("sql/define-roles-permissions-table-ddl.sql")
                .addScript("sql/define-users-table-ddl.sql")
                .addScript("sql/define-users-roles-table-ddl.sql")
                .addScript("sql/insert-roles-dml.sql")
                .addScript("sql/insert-roles-permissions-dml.sql")
                .addScript("sql/insert-users-dml.sql")
                .addScript("sql/insert-users-roles-dml.sql")
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
    SimpleSecurityManager securityManager(SecurityRepository securityRepository) {
        return new SimpleSecurityManager(securityRepository);
    }
}

