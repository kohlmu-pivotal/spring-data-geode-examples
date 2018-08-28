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

package example.springdata.geode.client.security.kt.shiro.realm

import example.springdata.geode.client.security.kt.domain.User
import example.springdata.geode.client.security.kt.server.repo.SecurityRepository
import example.springdata.geode.client.security.kt.shiro.authc.UserAuthenticationInfo.Companion.newAuthenticationInfo
import example.springdata.geode.client.security.kt.shiro.authz.UserAuthorizationInfo.Companion.newAuthorizationInfo
import example.springdata.geode.client.security.kt.shiro.authz.support.ComposableAuthorizationInfo
import example.springdata.geode.client.security.kt.shiro.realm.support.CredentialsMatcher.Companion.newCredentialsMatcher
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.AuthenticationInfo
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import org.springframework.util.Assert
import java.security.Principal

/**
 * The [SecurityRepositoryAuthorizingRealm] class is an Adapter and a Apache Shiro
 * [org.apache.shiro.realm.Realm] used to access security configuration meta-data
 * using a [SecurityRepository].
 *
 * @author John Blum
 * @see AuthorizingRealm
 *
 * @see example.app.geode.security.model.User
 *
 * @see SecurityRepository
 *
 * @since 1.0.0
 */

/**
 * Constructs an instance of the [SecurityRepositoryAuthorizingRealm] initialized with a [SecurityRepository]
 * containing security configuration meta-data to enforce authentication/authorization in a Apache Shiro secured
 * application.
 *
 * @param securityRepository [SecurityRepository] used by this [org.apache.shiro.realm.Realm] to
 * access security configuration meta-data (authentication/authorization).
 * @throws IllegalArgumentException if [SecurityRepository] is null.
 * @see example.springdata.geode.client.security.kt.server.repo.SecurityRepository
 */
class SecurityRepositoryAuthorizingRealm<T : User>(private val securityRepository: SecurityRepository<T>) : AuthorizingRealm() {

    private val isRealmAuthenticationRequired: Boolean
        get() = credentialsMatcher == null

    init {
        Assert.notNull(securityRepository, "SecurityRepository must not be null")
    }

    /**
     * @inheritDoc
     */
    @Throws(AuthenticationException::class)
    override fun doGetAuthenticationInfo(token: AuthenticationToken): AuthenticationInfo =
            newAuthenticationInfo(authenticate(token), this)

    private fun authenticate(token: AuthenticationToken): User {
        val user = resolveUser(token)

        if (isNotIdentified(user, token)) {
            throw AuthenticationException(String.format("User [%s] could not be authenticated", user))
        }

        return user!!
    }

    private fun resolveUser(token: AuthenticationToken): User? =
            resolveUser(token.principal)

    private fun isNotIdentified(user: User?, token: AuthenticationToken): Boolean =
            isRealmAuthenticationRequired && !isIdentified(user, token)

    private fun isIdentified(user: User?, token: AuthenticationToken): Boolean {
        return user != null && newCredentialsMatcher().match(user.credentials!!, token.credentials)
        //return (user != null && user.getCredentials().equals(token.getCredentials()));
    }

    /**
     * @inheritDoc
     */
    override fun doGetAuthorizationInfo(principalCollection: PrincipalCollection): AuthorizationInfo? {
        val primaryPrincipal = principalCollection.primaryPrincipal

        var current = compose(null, primaryPrincipal)

        principalCollection
                .filter { it != null && it != primaryPrincipal }
                .forEach { current = compose(current, it!!) }

        return current
    }

    private fun compose(current: AuthorizationInfo?, principal: Any): AuthorizationInfo? =
            compose(current, resolveUser(principal))

    private fun compose(current: AuthorizationInfo?, user: User?): AuthorizationInfo? =
            user?.let {
                ComposableAuthorizationInfo.compose(current, newAuthorizationInfo(user))
            } ?: current

    private fun resolveUser(principal: Any): User? =
            principal as? User ?: securityRepository.findBy(resolveUsername(principal))

    private fun resolveUsername(principal: Any): String =
            when (principal) {
                is User -> principal.name
                is Principal -> principal.name
                else -> principal.toString()
            }
}
