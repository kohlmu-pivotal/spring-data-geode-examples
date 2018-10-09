package example.springdata.geode.client.security.kt.server.repo

import example.springdata.geode.client.security.kt.domain.Role
import example.springdata.geode.client.security.kt.domain.User
import org.apache.geode.security.ResourcePermission
import org.jdom2.input.SAXBuilder
import org.springframework.beans.factory.InitializingBean
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource

/**
 * The [XmlSecurityRepository] class is a [example.app.geode.security.repository.SecurityRepository]
 * implementation that accesses security configuration meta-data stored in an XML document, accessed via JDOM.
 *
 * @author John Blum
 * @author Udo Kohlmeyer
 * @see ResourcePermission
 * @see org.jdom2.Document
 * @see org.jdom2.input.SAXBuilder
 * @see org.springframework.beans.factory.InitializingBean
 * @see org.springframework.core.io.ClassPathResource
 * @see org.springframework.core.io.Resource
 * @see example.springdata.geode.client.security.kt.domain.Role
 * @see example.springdata.geode.client.security.kt.domain.User
 * @see example.springdata.geode.client.security.kt.server.repo.CachingSecurityRepository
 * @since 1.0.0
 */

/**
 * Constructs an instance of [XmlSecurityRepository] initialized with the given roles/permissions
 * and users/roles [resources][Resource].
 *
 * @param rolesPermissions [Resource] reference containing roles and their associated permissions
 * security configuration meta-data.
 * @param usersRoles [Resource] reference containing users and their associated roles
 * security configuration meta-data.
 * @throws IllegalArgumentException if either `rolesPermissions` or `usersRoles` [Resource]
 * references are null.
 * @see org.springframework.core.io.Resource
 */
class XmlSecurityRepository
@JvmOverloads constructor(
        private val rolesPermissions: Resource = ClassPathResource(ROLES_PERMISSIONS_XML),
        private val usersRoles: Resource = ClassPathResource(USERS_ROLES_XML)) : CachingSecurityRepository(), InitializingBean {

    companion object {
        private const val ROLES_PERMISSIONS_XML = "roles-permissions.xml"
        private const val USERS_ROLES_XML = "users-roles.xml"
    }

    /**
     * @inheritDoc
     */
    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        saveAll(parseUsersRoles(usersRoles, parseRolesPermissions(rolesPermissions)))
    }

    @Throws(Exception::class)
    private fun parseRolesPermissions(rolesPermissions: Resource): Map<String, Role> =
            SAXBuilder().build(rolesPermissions.inputStream)?.let { document ->
                document.rootElement?.children?.mapNotNull { roleElement ->
                    Role.newRole(roleElement.getAttributeValue("name")).also { role ->
                        roleElement.getChild("permissions")?.children?.forEach { permissionElement ->
                            role.withPermissions(
                                    ResourcePermission(
                                            permissionElement.getAttributeValue("resource"),
                                            permissionElement.getAttributeValue("operation"),
                                            permissionElement.getAttributeValue("region"),
                                            permissionElement.getAttributeValue("key")))
                        }
                    }
                }
            }?.associate { it.getName() to it } ?: emptyMap()

    @Throws(Exception::class)
    private fun parseUsersRoles(usersRoles: Resource, roles: Map<String, Role>): List<User> =
            SAXBuilder().build(usersRoles.inputStream)?.let { document ->
                document.rootElement?.children?.mapNotNull { userElement ->
                    User.newUser(userElement.getAttributeValue("name"))
                            .withCredentials(userElement.getAttributeValue("password")).also { user ->
                                userElement.getChild("roles")?.children?.forEach { roleElement ->
                                    roleElement.getAttributeValue("name")?.also { roleName ->
                                        roles[roleName]?.let { role ->
                                            user.withRoles(role)
                                        }
                                    }
                                }
                            }
                }
            }?.toList() ?: emptyList()
}
