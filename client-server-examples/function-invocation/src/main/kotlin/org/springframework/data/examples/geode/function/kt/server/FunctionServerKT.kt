package org.springframework.data.examples.geode.function.kt.server

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.examples.geode.common.kt.server.ServerKT

@SpringBootApplication(scanBasePackageClasses = [FunctionServerKT::class])
class FunctionServerKT : ServerKT()

fun main(args: Array<String>) {
    SpringApplication.run(FunctionServerKT::class.java, *args)
    System.err.println("Press <ENTER> to exit")
    readLine()
}