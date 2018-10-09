package example.springdata.geode.client.security.kt.domain

import org.apache.geode.security.ResourcePermission
import org.cp.elements.lang.Identifiable
import org.springframework.util.Assert
import java.io.Serializable
import java.security.Principal

/**
 * Constructs an instance of [User] initialized with the given username.
 *
 * @param name [String] indicating the name of the [User].
 * @throws IllegalArgumentException if the username was not specified.
 */
data class User(private val name: String, val roles: MutableSet<Role> = mutableSetOf()) :
        Comparable<User>, Cloneable, Principal, Serializable, Iterable<Role>, Identifiable<String> {

    var credentials: String? = null

    init {
        Assert.hasText(name, "Username must be specified")
    }

    override fun setId(p0: String?) {
        throw UnsupportedOperationException("Operation Not Supported")
    }

    override fun getId(): String = getName()

    override fun iterator(): Iterator<Role> = roles.toSet().iterator()

    /**
     * @inheritDoc
     */
    override fun getName(): String = name

    /**
     * @inheritDoc
     */
    @Throws(CloneNotSupportedException::class)
    override fun clone(): Any = copy(name = getName()).withCredentials(credentials).withRoles(roles)

    /**
     * @inheritDoc
     */
    override fun compareTo(other: User): Int = this.getName().compareTo(other.getName())

    /**
     * @inheritDoc
     */
    override fun equals(other: Any?): Boolean {
        if (other is User) {
            if (other === this) {
                return true
            }
            return name == other.name
        }
        return false;
    }

    /**
     * @inheritDoc
     */
    override fun hashCode(): Int = (17 * 37 + name.hashCode())

    /**
     * Determines whether this [User] has been granted (assigned) the given [permission][ResourcePermission].
     *
     * @param permission [ResourcePermission] to evalute.
     * @return a boolean value indicating whether this [User] has been granted (assigned)
     * the given [ResourcePermission].
     * @see Role.hasPermission
     * @see ResourcePermission
     */
    fun hasPermission(permission: ResourcePermission) =
            roles.firstOrNull { it.hasPermission(permission) }?.let { true } ?: false

    /**
     * Determines whether this [User] has the specified [Role].
     *
     * @param role [Role] to evaluate.
     * @return a boolean value indicating whether this [User] has the specified [Role].
     * @see Role
     */
    fun hasRole(role: Role) = roles.contains(role)


    /**
     * @inheritDoc
     */
    override fun toString(): String = name

    /**
     * Adds the array of [Roles][Role] granting (resource) permissions to this [User].
     *
     * @param roles array of [Roles][Role] granting (resource) permissions to this [User].
     * @return this [User].
     * @see Role
     */
    fun withRoles(vararg roles: Role): User {
        this.roles.addAll(roles)
        return this
    }

    fun withRoles(roles: Collection<Role>): User {
        this.roles.addAll(roles)
        return this
    }

    /**
     * Sets this [User&#39;s][User] credentials (e.g. password) to the given value.
     *
     * @param credentials [String] containing this [User&#39;s][User] credentials (e.g. password).
     * @return this [User].
     * @see User
     */
    fun withCredentials(credentials: String?): User {
        this.credentials = credentials
        return this
    }

    companion object {

        /**
         * Factory method to construct a new [User] initialized with the given username.
         *
         * @param username [String] indicating the name of the [User].
         * @return a new [User] initialized with the given username.
         * @throws IllegalArgumentException if the username was not specified.
         * @see User
         */
        fun newUser(username: String): User = User(username)
    }
}
