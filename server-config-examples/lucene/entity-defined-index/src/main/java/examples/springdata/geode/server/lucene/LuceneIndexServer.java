package examples.springdata.geode.server.lucene;

import examples.springdata.geode.server.lucene.config.EntityDefinedLuceneIndexServerConfig;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackageClasses = EntityDefinedLuceneIndexServerConfig.class)
public class LuceneIndexServer {

    public static void main(String[] args) {
        new SpringApplicationBuilder(LuceneIndexServer.class).web(WebApplicationType.NONE).build().run(args);
    }
}