package example.springdata.geode.client.security.server.config;

import examples.springdata.geode.client.common.server.config.ServerApplicationConfig;
import org.apache.geode.internal.security.shiro.GeodePermissionResolver;
import org.apache.shiro.realm.text.PropertiesRealm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.config.annotation.EnableSecurity;

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

