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

package example.springdata.geode.client.security.kt.domain

import org.apache.geode.security.ResourcePermission
import org.cp.elements.lang.Identifiable
import org.springframework.util.Assert
import java.io.Serializable
import java.util.*

/**
 * The [Role] class is an Abstract Data Type (ADT) modeling a role of a user (e.g. Admin).
 *
 * @author John Blum
 * @see Serializable
 * @see Comparable
 * @see Iterable
 * @see ResourcePermission
 * @see org.cp.elements.lang.Identifiable
 *
 * @since 1.0.0
 */

/**
 * Constructs an instance of [Role] with the given name.
 *
 * @param name [String] name of this [Role].
 * @throws IllegalArgumentException if name was not specified.
 */
data class Role(private val name: String) : Comparable<Role>, Identifiable<String>, Iterable<ResourcePermission>, Serializable {
    val permissions = HashSet<ResourcePermission>()

    fun getName(): String = name

    override fun getId(): String = name

    override fun setId(id: String?) {
        throw UnsupportedOperationException("Operation Not Supported")
    }

    init {
        Assert.hasText(name, "Name must be specified")
    }

    /**
     * @inheritDoc
     */
    override fun compareTo(other: Role): Int = name.compareTo(other.name)

    /**
     * @inheritDoc
     */
    override fun equals(other: Any?): Boolean {
        if (other is Role) {
            if (other === this) {
                return true
            }

            return name == other.name
        }
        return false
    }

    /**
     * @inheritDoc
     */
    override fun hashCode(): Int = 17 * 37 + name.hashCode()

    /**
     * Determines whether this [Role] has been assigned (granted) the given [permission][ResourcePermission].
     *
     * @param permission [ResourcePermission] to evaluate.
     * @return a boolean value indicating whether this [Role] has been assigned (granted)
     * the given [permission][ResourcePermission].
     * @see ResourcePermission
     */
    fun hasPermission(permission: ResourcePermission): Boolean = this.permissions.contains(permission)

    /**
     * @inheritDoc
     */
    override fun iterator(): Iterator<ResourcePermission> = this.permissions.toSet().iterator()

    /**
     * @inheritDoc
     */
    override fun toString(): String = name

    /**
     * Adds (assigns/grants) all given [persmissions][ResourcePermission] to this [Role].
     *
     * @param permissions [ResourcePermission]s to assign/grant to this [Role].
     * @return this [Role].
     * @see ResourcePermission
     */
    fun withPermissions(vararg permissions: ResourcePermission): Role {
        this.permissions.addAll(permissions)
        return this
    }

    /**
     * Adds (assigns/grants) all given [persmissions][ResourcePermission] to this [Role].
     *
     * @param permissions [ResourcePermission]s to assign/grant to this [Role].
     * @return this [Role].
     * @see ResourcePermission
     */
    fun withPersmissions(permissions: Iterable<ResourcePermission>): Role {
        this.permissions.addAll(permissions)
        return this
    }

    companion object {

        /**
         * Factory method used to construct a new instance of [Role] initialized with the given name.
         *
         * @param name [String] indicating the name of the new [Role].
         * @return a new [Role] initialized with the given name.
         * @see Role
         */
        fun newRole(name: String): Role {
            return Role(name)
        }
    }
}
