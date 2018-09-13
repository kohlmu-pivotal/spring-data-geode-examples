package examples.springdata.geode.client.cq.consumer;


import org.apache.geode.cache.query.CqEvent;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableContinuousQueries;
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery;

import java.util.Scanner;

import static org.springframework.data.gemfire.config.annotation.ClientCacheApplication.Locator;

/**
 * @author Udo Kohlmeyer
 */
@SpringBootApplication(scanBasePackageClasses = CQConsumerClient.class)
@ClientCacheApplication(name = "CQConsumerClientCache", logLevel = "info", pingInterval = 5000L, readTimeout = 15000,
        retryAttempts = 1, subscriptionEnabled = true, locators = @Locator,
        readyForEvents = true, durableClientId = "22", durableClientTimeout = 5)
@EnableContinuousQueries
public class CQConsumerClient {
    public static void main(String[] args) {
        SpringApplication.run(CQConsumerClient.class, args);
    }

    @Bean
    ApplicationRunner runner() {
        return args -> new Scanner(System.in, "UTF-8").nextLine();
    }

    @ContinuousQuery(name = "CustomerJudeCQ", query = "SELECT * FROM /Customers", durable = true)
    public void handleEvent(CqEvent event) {
        System.out.println("Received message for CQ 'CustomerJudeCQ'" + event);
    }
}