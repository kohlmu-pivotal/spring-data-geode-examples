package examples.springdata.geode.client.security.server;

import examples.springdata.geode.client.security.server.config.SecurityEnabledServerConfiguration;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication(scanBasePackageClasses = SecurityEnabledServerConfiguration.class)
public class SecurityEnabledServer {
    public static void main(String[] args) {
        SpringApplication.run(SecurityEnabledServer.class, args);
    }

    @Bean
    ApplicationRunner runner() {
        return args -> {
            System.err.println("Press <ENTER> to exit");
            new Scanner(System.in, "UTF-8").nextLine();
        };
    }
}