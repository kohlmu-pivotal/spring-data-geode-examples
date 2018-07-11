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

package example.springdata.geode.client.security.kt.shiro.authz

import example.springdata.geode.client.security.kt.domain.User
import example.springdata.geode.client.security.kt.shiro.authz.support.AuthorizationInfoSupport
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.authz.Permission
import org.cp.elements.lang.Assert

/**
 * The [UserAuthorizationInfo] class is an implementation of the Apache Shiro [AuthorizationInfo] interface
 * backed by a [User] object.
 *
 * @author John Blum
 * @see org.apache.shiro.authz.AuthorizationInfo
 * @see Role
 * @see User
 * @see AuthorizationInfoSupport
 *
 * @since 1.0.0
 */

/**
 * Construct an instance of the [UserAuthorizationInfo] class initialized with the given [User],
 * which backs the details of the Apache Shiro [AuthorizationInfo].
 *
 * @param user [User] object encapsulating the details of the Apache Shiro
 * [AuthorizationInfo].
 * @throws IllegalArgumentException if [User] is null.
 * @see User
 */
class UserAuthorizationInfo(protected val user: User) : AuthorizationInfoSupport() {

    override fun getRoles(): MutableCollection<String> = _roles
    override fun getObjectPermissions(): MutableCollection<Permission> = _objectPermissions

    /**
     * @inheritDoc
     */
    private val _roles: MutableSet<String> by lazy {
        user.roles.map { it.getName() }.toMutableSet()
    }

    /**
     * @inheritDoc
     */

    private val _objectPermissions: MutableSet<Permission> by lazy {
        val permissionsSet = mutableSetOf<Permission>()
        user.roles.forEach { it.permissions.forEach { permissionsSet.add(it) } }
        permissionsSet
    }

    init {
        Assert.notNull(user, "User must not be null")
    }

    companion object {

        /**
         * Factory method used to construct an instance of the [UserAuthorizationInfo] class initialized with
         * the given [User].
         *
         * @param user [User] backing the Apache Shiro [AuthorizationInfo].
         * @return a new instance of the [UserAuthorizationInfo] class initialized with the given [User].
         * @see UserAuthorizationInfo
         *
         * @see User
         */
        fun newAuthorizationInfo(user: User): UserAuthorizationInfo {
            return UserAuthorizationInfo(user)
        }
    }
}
