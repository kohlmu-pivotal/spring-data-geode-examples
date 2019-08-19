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

package examples.springdata.geode.client.security.kt.shiro.authc

import examples.springdata.geode.client.security.kt.domain.User
import org.apache.shiro.authc.AuthenticationInfo
import org.apache.shiro.realm.Realm
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.subject.SimplePrincipalCollection

/**
 * The UserAuthenticationInfo class...
 *
 * @author John Blum
 * @author Udo Kohlmeyer
 * @since 1.0.0
 */

/**
 * Constructs an instance of [UserAuthenticationInfo] initialized with the given [User]
 * and [Realm] from which the [User] data was loaded.
 *
 * @param user [User] backing this [AuthenticationInfo] implementation
 * @param realm [Realm] from which the [User] data was loaded.
 * @see User
 *
 * @see Realm
 */

class UserAuthenticationInfo(val user: User, private val realm: Realm) : AuthenticationInfo {
    /**
     * @inheritDoc
     */
    override fun getCredentials(): String? = user.credentials

    /**
     * @inheritDoc
     */
    override fun getPrincipals(): PrincipalCollection = SimplePrincipalCollection(user, realm.name)

    companion object {

        /**
         * Factory method used to construct a new instance of the [UserAuthenticationInfo] class initialized with
         * the given [User].
         *
         * @param user [User] backing this [AuthenticationInfo] implementation
         * @param realm [Realm] from which the [User] data was loaded.
         * @return an instance of [UserAuthenticationInfo] initialized with the given [User] and [Realm].
         * @see User
         *
         * @see Realm
         */
        fun newAuthenticationInfo(user: User, realm: Realm): UserAuthenticationInfo {
            return UserAuthenticationInfo(user, realm)
        }
    }
}
