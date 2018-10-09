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

import example.springdata.geode.client.security.kt.domain.Constants
import org.apache.geode.security.AuthenticationFailedException
import org.apache.geode.security.ResourcePermission
import org.slf4j.LoggerFactory
import java.security.Principal
import java.util.*

/**
 * The [SecurityManagerSupport] class is an Apache Geode [SecurityManager] interface adapter providing
 * default implementations of the [SecurityManager] interface operations.
 *
 * @author John Blum
 * @see Principal
 *
 * @see ResourcePermission
 *
 * @see org.apache.geode.security.SecurityManager
 *
 * @since 1.0.0
 */
abstract class SecurityManagerSupport : org.apache.geode.security.SecurityManager {

    protected val logger = LoggerFactory.getLogger(javaClass)

    /* (non-Javadoc)*/
    protected fun getName(principal: Any): String {
        return if (principal is Principal) principal.name else principal.toString()
    }

    /* (non-Javadoc)*/
    protected fun getPassword(securityProperties: Properties): String {
        return getPropertyValue(securityProperties, Constants.SECURITY_PASSWORD_PROPERTY)
    }

    /* (non-Javadoc)*/
    protected fun getUsername(securityProperties: Properties): String {
        return getPropertyValue(securityProperties, Constants.SECURITY_USERNAME_PROPERTY)
    }

    /* (non-Javadoc)*/
    protected fun getPropertyValue(properties: Properties, propertyName: String): String {
        return properties.getProperty(propertyName)
    }

    /* (non-Javadoc)*/
    protected fun logDebug(message: String, vararg args: Any) {
        if (logger.isDebugEnabled) {
            logger.debug(message, args)
        }
    }

    /**
     * @inheritDoc
     */
    override fun init(securityProperties: Properties?) {
        if (logger.isDebugEnabled) {
            logger.debug("Security Properties [{}]", securityProperties)
        }
    }

    /**
     * @inheritDoc
     */
    @Throws(AuthenticationFailedException::class)
    override fun authenticate(securityProperties: Properties): Any? {
        return null
    }

    /**
     * @inheritDoc
     */
    override fun authorize(principal: Any?, permission: ResourcePermission?): Boolean {
        return principal != null
    }

    /**
     * @inheritDoc
     */
    override fun close() {
        if (logger.isDebugEnabled) {
            logger.debug("Closing SecurityManager [{}]", javaClass.name)
        }
    }
}
