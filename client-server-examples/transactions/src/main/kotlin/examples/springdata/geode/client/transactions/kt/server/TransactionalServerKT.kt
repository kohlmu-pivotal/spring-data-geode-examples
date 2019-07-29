package examples.springdata.geode.client.transactions.kt.server

import examples.springdata.geode.client.transactions.kt.server.config.TransactionalServerConfigKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackageClasses = [TransactionalServerConfigKT::class])
class TransactionalServerKT {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(TransactionalServerKT::class.java)
                    .web(WebApplicationType.NONE)
                    .build()
                    .run(*args)
        }
    }

    @Bean
    internal fun runner() = ApplicationRunner {
        System.err.println("Press <ENTER> to exit")
        readLine()
    }
}