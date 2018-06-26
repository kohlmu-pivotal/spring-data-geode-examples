package examples.springdata.geode.function.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication(scanBasePackageClasses = FunctionServerApplicationConfig.class)
public class FunctionServer {

	public static void main(String[] args) {
		SpringApplication.run(FunctionServer.class, args);
		System.err.println("Press <ENTER> to exit");
		new Scanner(System.in).nextLine();
	}
}
