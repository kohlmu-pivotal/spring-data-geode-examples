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
import org.apache.shiro.authz.Permission
import java.util.*

/**
 * The [ComposableAuthorizationInfo] class is an Apache Shiro [AuthorizationInfo] implementation
 * that composes multiple Apache Shiro [AuthorizationInfo] objects into a composition acting as a
 * single [AuthorizationInfo].
 *
 * @author John Blum
 * @see AuthorizationInfo
 *
 * @see example.app.shiro.authz.support.AuthorizationInfoSupport
 *
 * @since 1.0.0
 */
class ComposableAuthorizationInfo
/**
 * Constructs an instance of the [ComposableAuthorizationInfo] class compose of 2 given Apache Shiro
 * [AuthorizationInfo] objects.
 *
 * @param one first [AuthorizationInfo] in the composition.
 * @param two second [AuthorizationInfo] in the composition.
 * @see AuthorizationInfo
 */
private constructor(private val one: AuthorizationInfo, private val two: AuthorizationInfo) : AuthorizationInfoSupport() {

    /**
     * @inheritDoc
     */
    override fun getRoles(): Collection<String> {
        val roles = HashSet(this.one.roles)
        roles.addAll(this.two.roles)
        return roles
    }

    /**
     * @inheritDoc
     */
    override fun getObjectPermissions(): Collection<Permission> {
        val permissions = HashSet(this.one.objectPermissions)
        permissions.addAll(this.two.objectPermissions)
        return permissions
    }

    companion object {

        /**
         * Composes an array of [AuthorizationInfo] objects into a composition acting as
         * a single [AuthorizationInfo].
         *
         * @param array array of [AuthorizationInfo] objects to compose.
         * @return an instance of [ComposableAuthorizationInfo] composed of the array
         * of [AuthorizationInfo] objects.
         * @throws NullPointerException if the array is null.
         * @see AuthorizationInfo
         *
         * @see .compose
         */
        fun compose(vararg array: AuthorizationInfo): AuthorizationInfo? {
            return compose(Arrays.asList(*array))
        }

        /**
         * Composes an [Iterable] of [AuthorizationInfo] objects into a composition acting as
         * a single [AuthorizationInfo].
         *
         * @param iterable [Iterable] of [AuthorizationInfo] objects to compose.
         * @return an instance of [ComposableAuthorizationInfo] composed of the [Iterable]
         * of [AuthorizationInfo] objects.
         * @throws NullPointerException if the [Iterable] is null.
         * @see AuthorizationInfo
         *
         * @see .compose
         */
        fun compose(iterable: Iterable<AuthorizationInfo>): AuthorizationInfo? {
            var current: AuthorizationInfo? = null

            for (authorizationInfo in iterable) {
                current = compose(current, authorizationInfo)
            }

            return current
        }

        /**
         * Composes two Apache Shiro [AuthorizationInfo] objects into a composition, acting as a single instance
         * of the Apache Shiro [AuthorizationInfo] interface.
         *
         * @param one first [AuthorizationInfo] in the composition.
         * @param two second [AuthorizationInfo] in the composition.
         * @return `two` if `one` is null, `one` if `two` is null
         * or an instance of [ComposableAuthorizationInfo] if neither `one` or `two` is null.
         * @see example.app.shiro.authz.support.ComposableAuthorizationInfo
         *
         * @see AuthorizationInfo
         */
        fun compose(one: AuthorizationInfo?, two: AuthorizationInfo?): AuthorizationInfo? {
            return if (one == null) two else if (two == null) one else ComposableAuthorizationInfo(one, two)
        }
    }
}
