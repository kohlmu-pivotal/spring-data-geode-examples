/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package examples.springdata.geode.client.domain

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import org.springframework.util.StringUtils
import java.io.Serializable
import java.util.regex.Pattern

/**
 * Value object to represent email addresses.
 *
 * @author Oliver Gierke
 * @author Udo Kohlmeyer
 */
data class EmailAddress(val value: String) : Serializable {
    init {
        Assert.isTrue(isValid(value), "Invalid email address!")
    }

    @Component
    internal class EmailAddressToStringConverter : Converter<EmailAddress, String> {

        /*
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
		 */
        override fun convert(source: EmailAddress): String = source?.value
    }

    @Component
    internal class StringToEmailAddressConverter : Converter<String, EmailAddress> {
        /*
		 * (non-Javadoc)
		 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
		 */
        override fun convert(source: String): EmailAddress = StringUtils.hasText(source)?.let { EmailAddress(source) }
    }

    companion object {
        private const val serialVersionUID = -2990839949384592331L
        private val EMAIL_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        private val PATTERN = Pattern.compile(EMAIL_REGEX)

        /**
         * Returns whether the given value is a valid [EmailAddress].
         *
         * @param source must not be null or empty.
         * @return
         */
        fun isValid(emailAddress: String): Boolean {
            Assert.hasText(emailAddress, "email address cannot be empty")
            return PATTERN.matcher(emailAddress).matches()
        }
    }
}
