package examples.springdata.geode.server.wan.client;

import examples.springdata.geode.client.common.client.BaseClient;
import examples.springdata.geode.server.wan.client.config.BasicWANClientApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Creates a client to demonstrate basic CRUD operations. This client can be configured in 2 ways,
 * depending on profile selected. "proxy" profile will create a region with PROXY configuration that
 * will store no data locally. "localCache" will create a region that stores data in the local
 * client, to satisfy the "near cache" paradigm.
 *
 * @author Udo Kohlmeyer
 */
@SpringBootApplication(scanBasePackageClasses = BasicWANClientApplicationConfig.class)
public class WANBasicClient implements BaseClient {

    public static void main(String[] args) {
        SpringApplication.run(WANBasicClient.class, args);
    }
}