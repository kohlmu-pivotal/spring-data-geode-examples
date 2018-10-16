package examples.springdata.geode.server.wan.transport;

import examples.springdata.geode.server.wan.repo.CustomerRepository;
import examples.springdata.geode.server.wan.transport.config.WanTransportListenerConfig;
import examples.springdata.geode.util.DataCreatorsKt;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Scanner;

@SpringBootApplication(scanBasePackageClasses = WanTransportListenerConfig.class)
public class WanTransportListenerServer {
    public static void main(String[] args) {
        new SpringApplicationBuilder(WanTransportListenerServer.class)
                .web(WebApplicationType.NONE)
                .build()
                .run(args);
    }

    @Bean
    @Profile({"default", "SiteA"})
    public ApplicationRunner siteARunner() {
        return args -> new Scanner(System.in).nextLine();
    }

    @Bean
    @Profile("SiteB")
    public ApplicationRunner siteBRunner(CustomerRepository customerRepository) {
        return args -> {
            System.out.println("Inserting 300 customers");
            DataCreatorsKt.createCustomers(300, customerRepository);
        };
    }

}
