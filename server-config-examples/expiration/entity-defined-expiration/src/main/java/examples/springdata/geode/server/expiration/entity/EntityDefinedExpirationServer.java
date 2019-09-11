package examples.springdata.geode.server.expiration.entity;

import examples.springdata.geode.server.expiration.entity.config.EntityDefinedExpirationServerConfig;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackageClasses = EntityDefinedExpirationServerConfig.class)
public class EntityDefinedExpirationServer {

    public static void main(String[] args) {
        new SpringApplicationBuilder(EntityDefinedExpirationServer.class).web(WebApplicationType.NONE).build().run(args);
    }
}
