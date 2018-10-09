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

import example.springdata.geode.client.security.kt.domain.User

/**
 * The [CachingSecurityRepository] class caches Security Configuration Meta-Data and is meant to be extended
 * by classes that are data store specified (e.g. JDBC/RDBMS, LDAP, etc).
 *
 * @author John Blum
 * @see User
 * @see SecurityRepository
 *
 * @since 1.0.0
 */
abstract class CachingSecurityRepository : SecurityRepository {
    private val users: MutableMap<String, User> = mutableMapOf()

    /**
     * @inheritDoc
     */
    override fun findAll(): Iterable<User> = users.values.asSequence().asIterable()

    override fun delete(user: User?): Boolean =
            user?.run { users.remove(this.name)?.let { true } ?: false } ?: false


    override fun save(user: User): User =
            users.put(user.name, user) ?: user

}
