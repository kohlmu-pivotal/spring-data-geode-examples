package org.springframework.data.examples.geode.function.server;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = FunctionServerApplicationConfig.class)
public class FunctionServer {

	public static void main(String[] args) {
		SpringApplication.run(FunctionServer.class, args);
		System.err.println("Press <ENTER> to exit");
		new Scanner(System.in).nextLine();
	}
}
