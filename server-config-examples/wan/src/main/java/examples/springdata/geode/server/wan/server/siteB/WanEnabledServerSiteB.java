package examples.springdata.geode.server.wan.server.siteB;

import examples.springdata.geode.server.wan.server.siteB.config.WanEnableServerSiteBConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication(scanBasePackageClasses = WanEnableServerSiteBConfig.class)
public class WanEnabledServerSiteB {
    public static void main(String[] args) {
        new SpringApplicationBuilder(WanEnabledServerSiteB.class)
                .web(WebApplicationType.NONE)
                .build()
                .run(args);
    }

    @Bean
    public ApplicationRunner siteBRunner() {
        return args -> new Scanner(System.in).nextLine();
    }
}
