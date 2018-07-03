package examples.springdata.geode.client.client.function.kt.server

import examples.springdata.geode.client.function.kt.server.config.FunctionServerApplicationConfigKT
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackageClasses = [FunctionServerApplicationConfigKT::class])
class FunctionServerKT

fun main(args: Array<String>) {
    SpringApplication.run(FunctionServerKT::class.java, *args)
    System.err.println("Press <ENTER> to exit")
    readLine()
}