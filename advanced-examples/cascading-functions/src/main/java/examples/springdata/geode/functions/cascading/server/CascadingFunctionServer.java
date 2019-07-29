package examples.springdata.geode.functions.cascading.server;

import examples.springdata.geode.functions.cascading.server.config.CascadingFunctionServerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.gemfire.config.annotation.EnableManager;

import java.util.Scanner;

@SpringBootApplication
@EnableManager(start = true)
@ComponentScan(basePackages = "examples.springdata.geode.functions.cascading.kt.server.functions")
@Import(CascadingFunctionServerConfig.class)
public class CascadingFunctionServer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplicationBuilder(CascadingFunctionServer.class)
                .web(WebApplicationType.NONE)
                .build();

        String profile = "default";
        if(args.length != 0) {
            profile = args[0];
        }

        springApplication.setAdditionalProfiles(profile);
        springApplication.run(args);
    }

    @Bean
    ApplicationRunner runner() {
        return args -> new Scanner(System.in).nextLine();
    }
}