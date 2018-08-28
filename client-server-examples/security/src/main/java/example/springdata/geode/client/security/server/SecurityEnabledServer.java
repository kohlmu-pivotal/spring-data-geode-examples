package example.springdata.geode.client.security.server;

import java.util.Scanner;

import example.springdata.geode.client.security.server.config.SecurityEnabledServerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = SecurityEnabledServerConfiguration.class)
public class SecurityEnabledServer {
    public static void main(String[] args) {
        SpringApplication.run(SecurityEnabledServer.class, args);
        System.err.println("Press <ENTER> to exit");
        new Scanner(System.in, "UTF-8");
    }
}
