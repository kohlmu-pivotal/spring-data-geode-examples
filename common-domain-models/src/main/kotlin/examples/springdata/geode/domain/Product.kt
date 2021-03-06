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

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.gemfire.mapping.annotation.Region
import org.springframework.util.Assert
import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.Entity

/**
 * A product used in the examples.
 *
 * @author Oliver Gierke
 * @author David Turanski
 * @author Udo Kohlmeyer
 */
@Region("Products")
@Entity
data class Product @JvmOverloads @PersistenceConstructor constructor(@Id @javax.persistence.Id val id: Long?,
                                                                     val name: String, val price: BigDecimal,
                                                                     val description: String? = null) : Serializable {
    @Transient
    private val attributes: MutableMap<String, String> = mutableMapOf()

    init {
        Assert.hasText(name, "Name must not be empty")
        Assert.isTrue(BigDecimal.ZERO < price, "Price must be greater than zero")
    }

    /**
     * Sets the attribute with the given name to the given value.
     *
     * @param name must not be null or empty.
     * @param value
     */
    fun addAttribute(name: String, value: String) {
        Assert.hasText(name, "Name is required")
        this.attributes[name] = value
    }

    fun getAttributes() = attributes.toMap()

    override fun toString(): String = "$name @ $price"

    companion object {
        private val serialVersionUID = 831295555713696643L
    }
}
