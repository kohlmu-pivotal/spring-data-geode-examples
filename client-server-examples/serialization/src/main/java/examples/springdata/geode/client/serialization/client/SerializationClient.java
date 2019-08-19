package examples.springdata.geode.client.serialization.client;/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import examples.springdata.geode.client.common.client.BaseClient;
import examples.springdata.geode.client.serialization.client.config.PdxSerializationClientConfig;
import examples.springdata.geode.client.serialization.client.services.CustomerService;

@SpringBootApplication(scanBasePackageClasses = PdxSerializationClientConfig.class)
public class SerializationClient implements BaseClient {

	public static void main(String[] args) {
		SpringApplication.run(SerializationClient.class, args);
	}

	@Bean
	ApplicationRunner runner(CustomerService customerService) {
		return args -> populateData(customerService);
	}
}