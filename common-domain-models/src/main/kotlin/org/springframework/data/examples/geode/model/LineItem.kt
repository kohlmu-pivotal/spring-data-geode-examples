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

import org.springframework.util.Assert
import java.io.Serializable
import java.math.BigDecimal

/**
 * @author Udo Kohlmeyer
 */
data class LineItem @JvmOverloads constructor(private val product: Product, private val amount: Int = 1) : Serializable {

    val unitPrice = product.price
    val productId = product.id
    val productName = product.name

    val total: BigDecimal
        get() = unitPrice.multiply(BigDecimal.valueOf(amount.toLong()))

    init {
        Assert.isTrue(amount > 0, "The amount of Products to be bought must be greater than 0")
    }

    override fun toString(): String = "Purchased $amount of Product $productName at $unitPrice for total of $total"

    companion object {
        private const val serialVersionUID = 6571108615511329726L
    }
}
