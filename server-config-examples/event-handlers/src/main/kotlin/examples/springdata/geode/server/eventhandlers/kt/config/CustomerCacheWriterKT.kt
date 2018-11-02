package examples.springdata.geode.server.eventhandlers.kt.config

import examples.springdata.geode.domain.Customer
import org.apache.geode.cache.util.CacheWriterAdapter

class CustomerCacheWriterKT : CacheWriterAdapter<Long, Customer>()
