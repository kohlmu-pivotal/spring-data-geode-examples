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

package examples.springdata.geode.client.security.kt.shiro.authz.support

import org.apache.shiro.authz.AuthorizationInfo

/**
 * The [AuthorizationInfoSupport] class is an abstract base class supporting the implementation of
 * the Apache Shiro [AuthorizationInfo] interface.
 *
 * @author John Blum
 * @author Udo Kohlmeyer
 * @see AuthorizationInfo
 *
 * @since 1.0.0
 */
abstract class AuthorizationInfoSupport : AuthorizationInfo {

    /**
     * @inheritDoc
     */
    override fun getStringPermissions(): Collection<String> = objectPermissions.map { it.toString() }.toMutableList()
}
