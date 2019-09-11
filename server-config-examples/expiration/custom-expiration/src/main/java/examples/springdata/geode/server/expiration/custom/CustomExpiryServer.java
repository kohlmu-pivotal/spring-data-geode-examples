package examples.springdata.geode.server.expiration.custom;

import examples.springdata.geode.server.expiration.custom.config.CustomExpiryServerConfig;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackageClasses = CustomExpiryServerConfig.class)
public class CustomExpiryServer {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CustomExpiryServer.class).web(WebApplicationType.NONE).build().run(args);
    }
}
