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

package examples.springdata.geode.util

import org.apache.commons.logging.LogFactory
import org.apache.geode.cache.EntryEvent
import org.apache.geode.cache.util.CacheListenerAdapter
import org.springframework.stereotype.Component

/**
 * A simple CacheListener to log cache events
 *
 * @author David Turanski
 * @author Udo Kohlmeyer
 */
@Component
class LoggingCacheListener<K, V> : CacheListenerAdapter<K, V>() {

    private val log = LogFactory.getLog(LoggingCacheListener::class.java)

    override fun afterCreate(event: EntryEvent<K, V>) =
            log.info("In region [${event.region.name}] created key [${event.key}] value [${event.newValue}]")

    override fun afterDestroy(event: EntryEvent<K, V>) =
            log.info("In region [${event.region.name}] destroyed key [${event.key}] ")

    override fun afterUpdate(event: EntryEvent<K, V>) =
            log.info("In region [${event.region.name}] updated key [${event.key}] [oldValue [${event.newValue}]] new value [${event.newValue}]")
}
