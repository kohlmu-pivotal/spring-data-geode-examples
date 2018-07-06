package example.springdata.geode.client.security.server;

import example.springdata.geode.client.security.server.config.SecurityEnabledServerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication(scanBasePackageClasses = SecurityEnabledServerConfiguration.class)
public class SecurityEnabledServer {
    public static void main(String[] args) {
        SpringApplication.run(SecurityEnabledServer.class, args);
        System.err.println("Press <ENTER> to exit");
        new Scanner(System.in);
    }
}
