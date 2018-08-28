package examples.springdata.geode.client.common.server;

import java.util.Scanner;

import examples.springdata.geode.client.common.server.config.ServerApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = ServerApplicationConfig.class)
public class Server {

	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
		System.err.println("Press <ENTER> to exit");
		new Scanner(System.in, "UTF-8").nextLine();
	}
}
