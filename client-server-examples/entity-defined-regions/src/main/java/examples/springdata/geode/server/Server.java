package examples.springdata.geode.server;

import examples.springdata.geode.server.config.EntityDefinedRegionServerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication(scanBasePackageClasses = EntityDefinedRegionServerConfig.class)
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

    @Bean
    @SuppressWarnings("unused")
    ApplicationRunner runner() {
        return args -> {
            System.err.println("Press <ENTER> to exit");
            new Scanner(System.in, "UTF-8").nextLine();
        };
    }

}
