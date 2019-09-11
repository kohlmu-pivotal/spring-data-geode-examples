package examples.springdata.geode.server.expiration.entity.kt

import examples.springdata.geode.server.expiration.entity.kt.config.EntityDefinedExpirationServerConfigKT
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication(scanBasePackageClasses = arrayOf(EntityDefinedExpirationServerConfigKT::class))
class EntityDefinedExpirationServerKT {
}

fun main(args: Array<String>) {
    SpringApplicationBuilder(EntityDefinedExpirationServerKT::class.java).web(WebApplicationType.NONE).build().run(*args)
}
