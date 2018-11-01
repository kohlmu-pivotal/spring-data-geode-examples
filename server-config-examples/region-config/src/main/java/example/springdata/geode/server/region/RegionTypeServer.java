package example.springdata.geode.server.region;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class RegionTypeServer {
    public static void main(String[] args) {
        new SpringApplicationBuilder(RegionTypeServer.class)
                .web(WebApplicationType.NONE)
                .build()
                .run(args);
    }
}
