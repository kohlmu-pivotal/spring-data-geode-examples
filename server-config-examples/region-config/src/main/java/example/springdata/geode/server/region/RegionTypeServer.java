package example.springdata.geode.server.region;

import example.springdata.geode.server.region.config.RegionTypeConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackageClasses = RegionTypeConfiguration.class)
public class RegionTypeServer {
    public static void main(String[] args) {
        new SpringApplicationBuilder(RegionTypeServer.class)
            .web(WebApplicationType.NONE)
            .build()
            .run(args);
    }
}
