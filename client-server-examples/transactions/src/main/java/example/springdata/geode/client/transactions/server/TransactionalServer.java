package example.springdata.geode.client.transactions.server;

import example.springdata.geode.client.transactions.server.config.TransactionalServerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication(scanBasePackageClasses = TransactionalServerConfig.class)
public class TransactionalServer {
    public static void main(String[] args) {
        new SpringApplicationBuilder(TransactionalServer.class)
            .web(WebApplicationType.NONE)
            .build()
            .run(args);
    }

    @Bean
    ApplicationRunner runner() {
        return args -> {
            System.err.println("Press <ENTER> to exit");
            new Scanner(System.in, "UTF-8").nextLine();
        };
    }
}