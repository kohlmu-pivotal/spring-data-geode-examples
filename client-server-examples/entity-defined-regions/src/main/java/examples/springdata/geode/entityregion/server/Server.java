package examples.springdata.geode.entityregion.server;

import examples.springdata.geode.entityregion.server.config.EntityDefinedRegionServerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication(scanBasePackageClasses = EntityDefinedRegionServerConfig.class)
public class Server {
    public static void main(String[] args) {
        new SpringApplicationBuilder(Server.class)
                .web(WebApplicationType.NONE)
                .build()
                .run(args);
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
