package examples.springdata.geode.client.entityregion.server;

import examples.springdata.geode.client.entityregion.server.config.EntityDefinedRegionServerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication(scanBasePackageClasses = EntityDefinedRegionServerConfig.class)
public class EntityDefinedRegionServer {
    public static void main(String[] args) {
        new SpringApplicationBuilder(EntityDefinedRegionServer.class)
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
