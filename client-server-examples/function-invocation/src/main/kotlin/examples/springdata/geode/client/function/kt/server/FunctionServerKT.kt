package examples.springdata.geode.client.function.kt.server

import examples.springdata.geode.client.function.kt.server.config.FunctionServerApplicationConfigKT
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackageClasses = [FunctionServerApplicationConfigKT::class])
class FunctionServerKT() {
    @Bean
    fun runner(): ApplicationRunner = ApplicationRunner { _ ->
        System.err.println("Press <ENTER> to exit")
        readLine()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(FunctionServerKT::class.java, *args)
}