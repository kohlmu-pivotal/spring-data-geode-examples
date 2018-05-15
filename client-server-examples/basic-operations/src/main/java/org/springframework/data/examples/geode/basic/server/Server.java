package org.springframework.data.examples.geode.basic.server;

import java.io.IOException;
import java.util.Scanner;

import javax.annotation.Resource;

import org.apache.geode.cache.Region;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.examples.geode.domain.Customer;

@SpringBootApplication(scanBasePackages = "org.springframework.data.examples.geode.basic.server")
public class Server {

	@Resource
	private Region<Long, Customer> customerRegion;

	public static void main(String[] args) throws IOException {
		SpringApplication.run(Server.class, args);
		System.err.println("Press <ENTER> to exit");
		new Scanner(System.in).nextLine();
	}
}
