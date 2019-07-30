package examples.springdata.geode.clusterregion.server;

import java.util.Scanner;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import examples.springdata.geode.clusterregion.server.config.ClusterDefinedRegionServerConfig;

@SpringBootApplication(scanBasePackageClasses = ClusterDefinedRegionServerConfig.class)
public class ClusterDefinedRegionServer {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ClusterDefinedRegionServer.class)
			.web(WebApplicationType.NONE)
			.build()
			.run(args);
	}

	@Bean
	ApplicationRunner runner() {
		return args -> {
			System.err.println("Press <ENTER> to exit");
			new Scanner(System.in, "UTF-8").nextLine();
		};
	}
}
