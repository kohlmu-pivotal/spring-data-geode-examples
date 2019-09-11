package examples.springdata.geode.server.expiration.cache;

import examples.springdata.geode.server.expiration.cache.config.CacheDefinedExpirationServerConfig;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackageClasses = CacheDefinedExpirationServerConfig.class)
public class CacheDefinedExpirationServer {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CacheDefinedExpirationServer.class).web(WebApplicationType.NONE).build().run(args);
    }
}
