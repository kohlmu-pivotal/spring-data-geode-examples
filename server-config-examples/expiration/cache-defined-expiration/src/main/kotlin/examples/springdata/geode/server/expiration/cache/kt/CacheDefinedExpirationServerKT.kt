package examples.springdata.geode.server.expiration.cache.kt

import examples.springdata.geode.server.expiration.cache.kt.config.CacheDefinedExpirationServerConfigKT
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication(scanBasePackageClasses = [CacheDefinedExpirationServerConfigKT::class])
class CacheDefinedExpirationServerKT {

}

fun main(args: Array<String>) {
    SpringApplicationBuilder(CacheDefinedExpirationServerKT::class.java).web(WebApplicationType.NONE).build().run(*args)
}
