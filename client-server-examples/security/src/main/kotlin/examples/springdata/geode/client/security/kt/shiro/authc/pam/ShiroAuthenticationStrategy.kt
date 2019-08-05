package examples.springdata.geode.client.security.kt.shiro.authc.pam

import org.apache.shiro.authc.pam.AllSuccessfulStrategy
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy
import org.apache.shiro.authc.pam.AuthenticationStrategy
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy
import org.springframework.util.Assert

/**
 * The [ShiroAuthenticationStrategy] class is enumerated type that enumerates all the
 * Pluggable Authentication Modules (PAM) or strategies constituting when a Subject has been successfully identified.
 *
 * @author John Blum
 * @author Udo Kohlmeyer
 *
 * @see AllSuccessfulStrategy
 * @see AtLeastOneSuccessfulStrategy
 * @see AuthenticationStrategy
 * @see FirstSuccessfulStrategy
 * @since 1.0.0
 */
enum class ShiroAuthenticationStrategy
/**
 * Constructs an instance of the [ShiroAuthenticationStrategy] enum initialized with the specified
 * and corresponding Apache Shiro [AuthenticationStrategy] type and name for this enumerated value.
 *
 * @param type Apache Shiro [AuthenticationStrategy] class type.
 * @param strategyName name of this enumerated value.
 * @throws [AuthenticationStrategy] type or name are not specified.
 * @see AuthenticationStrategy
 */
constructor(val type: Class<out AuthenticationStrategy>, val strategyName: String) {
    ALL_SUCCESSFUL(AllSuccessfulStrategy::class.java, "allSuccessful"),
    AT_LEAST_ONE_SUCCESSFUL(AtLeastOneSuccessfulStrategy::class.java, "atLeastOneSuccessful"),
    FIRST_SUCCESSFUL(FirstSuccessfulStrategy::class.java, "firstSuccessful");

    init {
        Assert.hasText(name, "Name must be specified")
    }

    /**
     * @inheritDoc
     */
    override fun toString(): String {
        return name
    }

    companion object {
        fun findBy(type: Class<out AuthenticationStrategy>): ShiroAuthenticationStrategy? =
                values().firstOrNull { it.type == type }

        fun findBy(strategyName: String): ShiroAuthenticationStrategy? =
                values().firstOrNull { it.strategyName.equals(strategyName, ignoreCase = true) }
    }
}
