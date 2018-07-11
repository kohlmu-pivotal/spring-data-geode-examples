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

package example.springdata.geode.client.security.kt.shiro.realm.support

import org.apache.shiro.authc.AuthenticationInfo
import org.apache.shiro.authc.AuthenticationToken
import org.cp.elements.lang.ObjectUtils
import org.springframework.core.convert.converter.Converter
import java.security.MessageDigest

/**
 * The [CredentialsMatcher] class is used by the [SecurityRepositoryAuthorizingRealm]
 * as the default user authenticating, credential matching algorithm.
 *
 * @author John Blum
 * @see org.apache.shiro.authc.credential.CredentialsMatcher
 *
 * @see org.springframework.core.convert.converter.Converter
 *
 * @since 1.0.0
 */
class CredentialsMatcher : org.apache.shiro.authc.credential.CredentialsMatcher {

    private var converter: Converter<Any, ByteArray>? = null
        get() =
            ObjectUtils.defaultIfNull<Converter<Any, ByteArray>>(field, DEFAULT_OBJECT_TO_BYTE_ARRAY_CONVERTER)


    /**
     * @inheritDoc
     */
    override fun doCredentialsMatch(token: AuthenticationToken, info: AuthenticationInfo): Boolean {
        return match(info.credentials, token.credentials)
    }

    /**
     * Determines whether the `actualCredentials` match the `expectedCredentials`.
     *
     * @param expectedCredentials expected credentials stored for a valid user account.
     * @param actualCredentials actual credentials provided by a Subject during login.
     * @return a boolean value indicating whether the credentials match.
     * @see MessageDigest.isEqual
     * @see .getConverter
     */
    fun match(expectedCredentials: Any, actualCredentials: Any): Boolean {
        val converter = converter

        return MessageDigest.isEqual(converter?.convert(expectedCredentials), converter?.convert(actualCredentials))
    }

    /**
     * Configures this [CredentialsMatcher] with a given Spring [Converter] to convert an [Object]
     * to a `byte[]`.
     *
     * @param objectToByteArray [Object] to `byte[]` [Converter].
     * @return this [CredentialsMatcher].
     * @see .setConverter
     */
    fun with(objectToByteArray: Converter<Any, ByteArray>): CredentialsMatcher {
        converter = objectToByteArray
        return this
    }

    companion object {

        private val DEFAULT_OBJECT_TO_BYTE_ARRAY_CONVERTER: Converter<Any, ByteArray> = ObjectToByteArrayConverter.INSTANCE

        /**
         * Factory method to construct an uninitialized instance of the [CredentialsMatcher] class.
         *
         * @return a new instance of the [CredentialsMatcher].
         * @see example.app.shiro.realm.support.CredentialsMatcher
         */
        fun newCredentialsMatcher(): CredentialsMatcher {
            return CredentialsMatcher()
        }
    }
}
