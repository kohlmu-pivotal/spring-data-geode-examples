package examples.springdata.geode.server.compression;

import examples.springdata.geode.server.compression.config.CompressionEnabledServerConfig;
import examples.springdata.geode.server.compression.repo.CustomerRepository;
import examples.springdata.geode.util.DataCreatorsKt;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackageClasses = CompressionEnabledServerConfig.class)
public class CompressionEnabledServer {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CompressionEnabledServer.class).web(WebApplicationType.NONE).build().run(args);
    }

    @Bean
    ApplicationRunner runner(CustomerRepository customerRepo) {
        System.out.println("Inserting 4000 Customers into compressed region");
        return args -> DataCreatorsKt.createCustomers(4000, customerRepo);
    }
}
