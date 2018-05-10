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

package org.springframework.data.gemfire.examples.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.util.Assert;

/**
 * @author Oliver Gierke
 */
public class LineItem implements Serializable {

	private static final long serialVersionUID = 6571108615511329726L;

	private int amount;

	private BigDecimal price;

	private Long productId;

	private String productName;

	/**
	 * Creates a new {@link LineItem} for the given {@link Product}.
	 *
	 * @param product must not be {@literal null}.
	 */
	public LineItem(Product product) {
		this(product, 1);
	}

	/**
	 * Constructs a new {@link LineItem} for the given {@link Product} and amount.
	 *
	 * @param product must not be {@literal null}.
	 * @param amount the number of {@link Product Products} purchased.
	 */
	public LineItem(Product product, int amount) {

		Assert.notNull(product, "Product is required");
		Assert.isTrue(amount > 0, "The amount of Products to be bought must be greater than 0");

		this.productId = product.getId();
		this.productName = product.getName();
		this.amount = amount;
		this.price = product.getPrice();
	}

	/**
	 * Returns the id of the {@link Product} the {@link LineItem} refers to.
	 *
	 * @return
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * Returns the {@link String name} of the {@link Product} that this {@link LineItem} refers to.
	 *
	 * @return the {@link String name} of the {@link Product} that this {@link LineItem} refers to.
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * Returns the amount of {@link Product}s to be ordered.
	 *
	 * @return
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Returns the price a single unit of the {@link LineItem}'s product.
	 *
	 * @return the price
	 */
	public BigDecimal getUnitPrice() {
		return price;
	}

	/**
	 * Returns the total for the {@link LineItem}.
	 *
	 * @return
	 */
	public BigDecimal getTotal() {
		return price.multiply(BigDecimal.valueOf(amount));
	}

	@Override
	public String toString() {
		return String.format("Purchased [%d] of Product [%s] at [%s] for total of [%s]",
			getAmount(), getProductName(), getUnitPrice(), getTotal());
	}
}
