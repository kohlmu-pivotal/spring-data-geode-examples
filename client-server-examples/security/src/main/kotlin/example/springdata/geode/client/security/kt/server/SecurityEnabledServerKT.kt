package example.springdata.geode.client.security.kt.server

import example.springdata.geode.client.security.kt.server.config.SecurityEnabledServerConfigurationKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackageClasses = [SecurityEnabledServerConfigurationKT::class])
class SecurityEnabledServerKT {
    @Bean
    fun runner() = ApplicationRunner {
        System.err.println("Press <ENTER> to exit")
        readLine()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(SecurityEnabledServerKT::class.java, *args)
}