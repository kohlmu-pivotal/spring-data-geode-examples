package examples.springdata.geode.common.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication(scanBasePackageClasses = ServerApplicationConfig.class)
public class Server {

	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
		System.err.println("Press <ENTER> to exit");
		new Scanner(System.in).nextLine();
	}
}
