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

package examples.springdata.geode.domain

import org.springframework.util.Assert
import java.io.Serializable

/**
 * An address.
 *
 * @author Oliver Gierke
 * @author Udo Kohlmeyer
 */
data class Address(private val street: String, private val city: String, private val country: String) : Serializable {
    init {
        Assert.hasText(street, "Street is required")
        Assert.hasText(city, "City is required")
        Assert.hasText(country, "Country is required")
    }
}
