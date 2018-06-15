package org.springframework.data.examples.geode.cq.kt.client.consumer

/*
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

import org.apache.geode.cache.query.CqEvent
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableContinuousQueries
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery

@SpringBootApplication(scanBasePackageClasses = [StupidCQKT::class])
@ClientCacheApplication(name = "CQConsumerClientCache", logLevel = "info", pingInterval = 5000L, readTimeout = 15000,
        retryAttempts = 1, subscriptionEnabled = true, locators = [ClientCacheApplication.Locator(host = "localhost", port = 10334)])
@EnableContinuousQueries
class StupidCQKT {

    @ContinuousQuery(name = "CustomerJudeCQ2", query = "SELECT * FROM /Customers", durable = true)
    fun handleEvent(event: CqEvent) {
        println("Received message for CQ 'CustomerJudeCQ': $event")
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(StupidCQKT::class.java, *args)
    readLine()
}
