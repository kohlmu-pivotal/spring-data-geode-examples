package examples.springdata.geode.client.security.kt.server.repo

import examples.springdata.geode.client.security.kt.domain.Role
import examples.springdata.geode.client.security.kt.domain.User

/**
 * The [SecurityRepository] interface is a contract for Data Access Objects (DAO) implementing this interface
 * to perform CRUD and query operations on [User] information, pertinent to the security of the system.
 *
 * @author John Blum
 * @author Udo Kohlmeyer
 * @see Role
 * @see User
 * @see org.springframework.stereotype.Repository
 * @since 1.0.0
 */
interface SecurityRepository {

    /**
     * Finds all [Users][User] of the system.
     *
     * @return an [Iterable] of [Users][User] of the system.
     * @see User
     *
     * @see Iterable
     */
    fun findAll(): Iterable<User>

    /**
     * Deletes the given [User] from the system.
     *
     * @param user [User] to delete.
     * @return a boolean value indicating if the [User] was deleted successfully.
     * @see User
     */
    fun delete(user: User?): Boolean

    /**
     * Records (persist) the information (state) of the [User].
     *
     * @param user [User] to store.
     * @return the [User].
     * @see User
     */
    fun save(user: User): User

    /* (non-Javadoc) */
    fun count(): Int {
        val users = findAll()
        return when (users) {
            is Map<*, *> -> users.size
            else -> users.count()
        }
    }

    /* (non-Javadoc) */
    fun createUser(username: String, vararg roles: Role): User =
            save(User.newUser(username).withRoles(*roles))

    /* (non-Javadoc) */
    fun delete(username: String): Boolean =
            findBy(username)?.run { delete(this) } ?: false

    /* (non-Javadoc) */
    fun deleteAll(vararg username: String): Boolean =
            deleteAll(findAll(*username))

    /* (non-Javadoc) */
    fun deleteAll(vararg users: User): Boolean =
            deleteAll(users.asIterable())

    /* (non-Javadoc) */
    fun deleteAll(users: Iterable<User> = findAll()): Boolean =
            users.firstOrNull { !delete(it) }?.let { false } ?: true

    /* (non-Javadoc) */
    fun exists(username: String): Boolean =
            findBy(username) != null

    /* (non-Javadoc) */
    fun findAll(vararg username: String): Iterable<User> =
            findAll(username.asIterable())

    /* (non-Javadoc) */
    fun findAll(username: Iterable<String>): Iterable<User> =
            username.mapNotNull { findBy(it) }.toMutableList()

    /* (non-Javadoc) */
    fun findBy(username: String): User? = findAll().firstOrNull { it.name == username }

    /* (non-Javadoc) */
    fun saveAll(vararg users: User): Array<User> =
            saveAll(users.asIterable()).toList().toTypedArray()

    /* (non-Javadoc) */
    fun saveAll(users: Iterable<User>): Iterable<User> =
            users.map { save(it) }.toMutableList()
}
