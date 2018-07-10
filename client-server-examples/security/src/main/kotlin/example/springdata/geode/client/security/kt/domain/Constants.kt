/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package example.springdata.geode.client.security.kt.domain

/**
 * The [Constants] class contains properties and other constants used in the Apache Geode Integrated Security
 * framework.
 *
 * @author John Blum
 * @since 1.0.0
 */
interface Constants {

    companion object {
        const val SECURITY_PASSWORD_PROPERTY = "security-password"
        const val SECURITY_USERNAME_PROPERTY = "security-username"
    }


}
