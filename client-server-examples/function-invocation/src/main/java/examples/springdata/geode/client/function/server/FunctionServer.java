package examples.springdata.geode.client.function.server;

import java.util.Scanner;

import examples.springdata.geode.client.function.server.config.FunctionServerApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = FunctionServerApplicationConfig.class)
public class FunctionServer {

	public static void main(String[] args) {
		SpringApplication.run(FunctionServer.class, args);
		System.err.println("Press <ENTER> to exit");
		new Scanner(System.in, "UTF-8").nextLine();
	}
}
