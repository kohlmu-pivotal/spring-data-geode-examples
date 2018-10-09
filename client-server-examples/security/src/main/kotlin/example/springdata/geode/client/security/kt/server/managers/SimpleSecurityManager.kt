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

package example.springdata.geode.client.security.kt.server.managers

import example.springdata.geode.client.security.kt.domain.User
import example.springdata.geode.client.security.kt.server.repo.SecurityRepository
import example.springdata.geode.client.security.kt.server.repo.XmlSecurityRepository
import org.apache.geode.security.AuthenticationFailedException
import org.apache.geode.security.ResourcePermission
import org.apache.shiro.util.Assert
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.*

/**
 * The [SimpleSecurityManager] class is an example Apache Geode [SecurityManager] provider implementation
 * used to secure Apache Geode.
 *
 * @author John Blum
 * @see SecurityManagerSupport
 *
 * @see example.app.geode.security.model.Role
 *
 * @see example.app.geode.security.model.User
 *
 * @see example.app.geode.security.repository.SecurityRepository
 *
 * @see ResourcePermission
 *
 * @see org.apache.geode.security.SecurityManager
 *
 * @see org.springframework.stereotype.Service
 *
 * @since 1.0.0
 */
@Service
class SimpleSecurityManager
/**
 * Constructs an instance of the [SimpleSecurityManager] initialized with the given [SecurityRepository]
 * for accessing the data store containing security configuration meta-data used to secure Apache Geode.
 *
 * @param securityRepository [SecurityRepository] used to access the data store containing security
 * configuration meta-data used to secure Apache Geode.
 * @throws IllegalArgumentException if [SecurityRepository] is null.
 * @see example.app.geode.security.repository.SecurityRepository
 */
@JvmOverloads constructor(
        /**
         * Returns a reference to the [SecurityRepository] used to access the data store containing
         * security configuration meta-data used to secure Apache Geode.
         *
         * @return a reference to the [SecurityRepository].
         * @see example.app.geode.security.repository.SecurityRepository
         */
        protected val securityRepository: SecurityRepository = defaultSecurityRepository()) : SecurityManagerSupport() {

    companion object {

        /**
         * Factory method used to construct and initialize a default instance of the [SecurityRepository].
         * By default, Apache Geode Security is configured with XML security configuration meta-data.
         *
         * @return an instance of the [SecurityRepository] used as the default implementation when a
         * [SecurityRepository] is not explicitly configured.
         * @see example.app.geode.security.repository.support.XmlSecurityRepository
         *
         * @see example.app.geode.security.repository.SecurityRepository
         */
        private fun defaultSecurityRepository(): SecurityRepository {
            try {
                val securityRepository = XmlSecurityRepository()
                securityRepository.afterPropertiesSet()

                return securityRepository
            } catch (e: Exception) {
                throw RuntimeException(String.format(
                        "Failed to construct and initialize an instance of the SecurityRepository [%s]",
                        XmlSecurityRepository::class.java))
            }
        }
    }

    init {
        Assert.notNull(securityRepository, "SecurityRepository must not be null")
    }

    /**
     * @inheritDoc
     */
    @Throws(AuthenticationFailedException::class)
    override fun authenticate(securityProperties: Properties): Any? {
        val username = getUsername(securityProperties)
        val password = getPassword(securityProperties)

        logDebug("User with name [{}] is attempting to login with password [{}]", username, password)

        val user = securityRepository.findBy(username)

        if (isNotAuthentic(user!!, password)) {
            throw AuthenticationFailedException(String.format("Failed to authenticate user [%s]", username))
        }

        return user
    }

    /* (non-Javadoc) */
    // NOTE User credentials or provide credentials could be encrypted so this SecurityManager would know how to
    // decrypt or still compare the credentials.
    protected fun isAuthentic(username: String, credentials: String): Boolean {
        return isAuthentic(securityRepository.findBy(username), credentials)
    }

    /* (non-Javadoc) */
    protected fun isAuthentic(user: User?, credentials: String): Boolean {
        return user?.run { user.credentials == credentials } ?: false
    }

    /* (non-Javadoc) */
    protected fun isNotAuthentic(username: String, credentials: String): Boolean {
        return !isAuthentic(username, credentials)
    }

    /* (non-Javadoc) */
    protected fun isNotAuthentic(user: User, credentials: String): Boolean {
        return !isAuthentic(user, credentials)
    }

    /**
     * @inheritDoc
     */
    override fun authorize(principal: Any?, permission: ResourcePermission?): Boolean {
        logDebug("Principal [{}] is requesting access to a Resource {} with the required Permission [{}]",
                principal!!, permission!!.resource, permission)

        return isAuthorized(principal, permission)
    }

    /* (non-Javadoc) */
    protected fun isAuthorized(principal: Any?, permission: ResourcePermission): Boolean {
        val user = resolveUser(principal)

        return user != null && isAuthorized(user, permission)
    }

    /* (non-Javadoc) */
    protected fun resolveUser(principal: Any?): User? {
        return if (principal is User) principal else securityRepository.findBy(getName(principal!!))
    }

    /* (non-Javadoc) */
    protected fun isAuthorized(user: User, requiredPermission: ResourcePermission): Boolean {
        if (!user.hasPermission(requiredPermission)) {
            return user.roles.firstOrNull { role ->
                role.permissions.firstOrNull { userPermission -> isPermitted(userPermission, requiredPermission) }?.let { true }
                        ?: false
            }?.let { true } ?: false
        }

        return true
    }

    /* (non-Javadoc) */
    protected fun isPermitted(userPermission: ResourcePermission, resourcePermission: ResourcePermission): Boolean {
        return userPermission.implies(resourcePermission)
    }

    /* (non-Javadoc) */
    internal fun toPermissionDescriptor(permission: ResourcePermission): String {
        val builder = StringBuilder(permission.resource.name)

        builder.append(":").append(permission.operation.name)

        if (isRequiredPermissionAttribute(permission.regionName)) {
            builder.append(":").append(permission.regionName)
        }

        if (isRequiredPermissionAttribute(permission.key)) {
            builder.append(":").append(permission.key)
        }

        return builder.toString()
    }

    /* (non-Javadoc) */
    internal fun isRequiredPermissionAttribute(value: String): Boolean {
        return StringUtils.hasText(value) && "*" != value.trim { it <= ' ' }
    }
}
/**
 * Default constructor constructing an instance of the [SimpleSecurityManager] initialized with an instance
 * of the [XmlSecurityRepository], reading security configuration meta-data from a XML document/file.
 *
 * @see example.app.geode.security.repository.support.XmlSecurityRepository
 *
 * @see .SimpleSecurityManager
 * @see .defaultSecurityRepository
 */
