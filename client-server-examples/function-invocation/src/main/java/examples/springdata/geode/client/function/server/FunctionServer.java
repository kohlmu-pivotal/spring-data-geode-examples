package examples.springdata.geode.client.function.server;

import java.util.Scanner;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import examples.springdata.geode.client.function.server.config.FunctionServerApplicationConfig;

@SpringBootApplication(scanBasePackageClasses = FunctionServerApplicationConfig.class)
public class FunctionServer {

	@Bean
	ApplicationRunner runner() {
		return args -> {
			System.err.println("Press <ENTER> to exit");
			new Scanner(System.in, "UTF-8").nextLine();
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(FunctionServer.class, args);
	}
}
