package example.springdata.geode.server.syncqueues;

import example.springdata.geode.server.syncqueues.config.AsyncQueueServerConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = AsyncQueueServerConfig.class)
public class AsyncQueueServer {
}
