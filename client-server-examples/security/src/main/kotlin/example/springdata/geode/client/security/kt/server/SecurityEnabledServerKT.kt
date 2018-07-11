package example.springdata.geode.client.security.kt.server

import example.springdata.geode.client.security.kt.server.config.SecurityEnabledServerConfigurationKT
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackageClasses = [SecurityEnabledServerConfigurationKT::class])
class SecurityEnabledServerKT

fun main(args: Array<String>) {
    SpringApplication.run(SecurityEnabledServerKT::class.java, *args)
    System.err.println("Press <ENTER> to exit")
    readLine()
}