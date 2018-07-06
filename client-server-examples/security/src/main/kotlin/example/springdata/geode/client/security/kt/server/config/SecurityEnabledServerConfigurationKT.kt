package example.springdata.geode.client.security.kt.server.config

import examples.springdata.geode.client.common.kt.server.config.ServerApplicationConfigKT
import org.apache.geode.internal.security.shiro.GeodePermissionResolver
import org.apache.shiro.realm.text.PropertiesRealm
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.data.gemfire.config.annotation.EnableSecurity

@Configuration
@Import(ServerApplicationConfigKT::class)
class SecurityEnabledServerConfigurationKT

@Configuration
@EnableSecurity(shiroIniResourcePath = "shiro.ini")
@Profile("shiro-ini-configuration")
class ApacheShiroIniConfiguration

@Configuration
@EnableSecurity
@Profile("default", "shiro-ini-configuration")
class ApacheShiroRealmConfiguration {
    @Bean
    fun shiroRealm(): PropertiesRealm {

        val propertiesRealm = PropertiesRealm()

        propertiesRealm.setResourcePath("classpath:server/shiro.properties")
        propertiesRealm.permissionResolver = GeodePermissionResolver()

        return propertiesRealm
    }
}
