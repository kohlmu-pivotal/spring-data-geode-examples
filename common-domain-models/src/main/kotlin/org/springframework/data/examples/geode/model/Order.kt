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

package org.springframework.data.examples.geode.model

import org.springframework.data.annotation.Id
import org.springframework.data.gemfire.mapping.annotation.Region
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

/**
 * @author Oliver Gierke
 * @author David Turanski
 * @author Udo Kohlmeyer
 */
@Region
data class Order @JvmOverloads constructor(@Id @javax.persistence.Id val id: Long?, val customerId: Long, val billingAddress: Address, val shippingAddress: Address = billingAddress) : Serializable, Iterable<LineItem> {
    private val lineItems = HashSet<LineItem>()

    /**
     * Returns the total of the [Order].
     *
     * @return
     */
    val total: BigDecimal
        get() {

            var total = BigDecimal.ZERO

            for (item in this) {
                total = total.add(item.total)
            }

            return total
        }

    /**
     * Adds the given [LineItem] to the [Order].
     *
     * @param lineItem
     */
    fun add(lineItem: LineItem) = lineItems.add(lineItem)

    /**
     * Returns all [LineItem]s currently belonging to the [Order].
     *
     * @return
     */
    fun getLineItems(): Set<LineItem> = Collections.unmodifiableSet(lineItems)

    override fun iterator(): Iterator<LineItem> = getLineItems().iterator()

    override fun toString(): String = id?.toString() ?: "0"

    companion object {
        private val serialVersionUID = -3779061453639083037L
    }
}
