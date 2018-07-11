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

package example.springdata.geode.client.security.kt.server.repo

import example.springdata.geode.client.security.kt.domain.Role
import example.springdata.geode.client.security.kt.domain.Role.Companion.newRole
import org.apache.geode.security.ResourcePermission
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

/**
 * The [JdbcSecurityRepository] class is a [example.app.geode.security.repository.SecurityRepository]
 * implementation that accesses security configuration meta-data stored in an RDBMS, accessed via JDBC.
 *
 * @author John Blum
 * @see Role
 * @see User
 * @see CachingSecurityRepository
 * @see ResourcePermission
 * @see org.springframework.beans.factory.InitializingBean
 * @see org.springframework.jdbc.core.JdbcTemplate
 *
 * @since 1.0.0
 */

/**
 * Constructs an instance of the [JdbcSecurityRepository] initialized with the given [JdbcTemplate].
 *
 * @param jdbcTemplate [JdbcTemplate] used to query the JDBC [javax.sql.DataSource]
 * containing security configuration meta-data used to secure Apache Geode.
 * @throws IllegalArgumentException if [JdbcTemplate] is null.
 * @see org.springframework.jdbc.core.JdbcTemplate
 */
@Repository
class JdbcSecurityRepository(protected val jdbcTemplate: JdbcTemplate) : CachingSecurityRepository(), InitializingBean {
    protected val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        protected const val ROLES_QUERY = "SELECT name FROM geode_security.roles"

        protected const val ROLE_PERMISSIONS_QUERY = (""
                + " SELECT rolePerms.resource, rolePerms.operation, rolePerms.region_name, rolePerms.key_name"
                + " FROM geode_security.roles_permissions rolePerms"
                + " INNER JOIN geode_security.roles roles ON roles.id = rolePerms.role_id "
                + " WHERE roles.name = ?")

        protected const val USERS_QUERY = "SELECT name, credentials FROM geode_security.users"

        protected const val USER_ROLES_QUERY = (""
                + " SELECT roles.name"
                + " FROM geode_security.roles roles"
                + " INNER JOIN geode_security.users_roles userRoles ON roles.id = userRoles.role_id"
                + " INNER JOIN geode_security.users users ON userRoles.user_id = users.id"
                + " WHERE users.name = ?")
    }


    /**
     * @inheritDoc
     */
    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        val roles = jdbcTemplate.query(ROLES_QUERY) { resultSet, _ -> newRole(resultSet.getString(1)) }
        val roleMapping = HashMap<String, Role>(roles.size)

        roles.forEach {
            jdbcTemplate.query(ROLE_PERMISSIONS_QUERY, arrayOf(it.getName())
            ) { resultSet, _ ->
                it.withPermissions(newResourcePermission(resultSet.getString(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4)))
            }
            roleMapping[it.getName()] = it
        }

        val users = jdbcTemplate.query(USERS_QUERY
        ) { resultSet, _ -> createUser(resultSet.getString(1)).usingCredentials(resultSet.getString(2)) }

        users.forEach {
            jdbcTemplate.query(USER_ROLES_QUERY, arrayOf(it.name)
            ) { resultSet, _ -> it.withRoles(roleMapping[resultSet.getString(1)]!!) }

            save(it)
        }

        logger.debug("Users {}", users)
    }

    /* (non-Javadoc) */
    protected fun newResourcePermission(resource: String?, operation: String?, region: String?, key: String?): ResourcePermission {
        return ResourcePermission(resource, operation, region, key)
    }
}
