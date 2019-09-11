package examples.springdata.geode.server.expiration.custom.kt

import examples.springdata.geode.server.expiration.custom.kt.config.CustomExpiryServerConfigKT
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication(scanBasePackageClasses = [CustomExpiryServerConfigKT::class])
class CustomExpiryServerKT {

}

fun main(args: Array<String>) {
    SpringApplicationBuilder(CustomExpiryServerKT::class.java).web(WebApplicationType.NONE).build().run(*args)
}