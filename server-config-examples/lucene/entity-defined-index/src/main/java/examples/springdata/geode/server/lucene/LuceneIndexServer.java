package examples.springdata.geode.server.lucene;

import examples.springdata.geode.server.lucene.config.EntityDefinedLuceneIndexServerConfig;
import examples.springdata.geode.server.lucene.repo.LuceneCustomerRepo;
import org.apache.geode.cache.lucene.LuceneResultStruct;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.search.lucene.LuceneTemplate;

import java.util.List;

@SpringBootApplication(scanBasePackageClasses = EntityDefinedLuceneIndexServerConfig.class)
public class LuceneIndexServer {

    public static void main(String[] args) {
        new SpringApplicationBuilder(LuceneIndexServer.class).web(WebApplicationType.NONE).build().run(args);
    }

    @Bean
    ApplicationRunner runner(LuceneTemplate luceneTemplate, LuceneCustomerRepo luceneCustomerRepo) {
        return args -> {
            createCustomerData(luceneCustomerRepo);

            System.out.println("Completed creating orders ");

            final List<LuceneResultStruct<Object, Object>> lastName = luceneTemplate.query("lastName: D*", "lastName");
            System.out.println("lastName = " + lastName);
        };
    }

    private void createCustomerData(LuceneCustomerRepo customerRepository) {
        System.out.println("Inserting 300 customers");
        examples.springdata.geode.server.kt.lucene.domain.DataCreatorsKt.createLuceneCustomers(300, customerRepository);
    }
}