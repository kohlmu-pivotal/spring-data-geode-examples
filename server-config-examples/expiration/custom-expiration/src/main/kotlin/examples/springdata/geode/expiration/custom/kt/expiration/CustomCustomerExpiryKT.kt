package examples.springdata.geode.expiration.custom.kt.expiration

import examples.springdata.geode.domain.Customer
import org.apache.geode.cache.CustomExpiry
import org.apache.geode.cache.ExpirationAction
import org.apache.geode.cache.ExpirationAttributes
import org.apache.geode.cache.Region


class CustomCustomerExpiryKT(private val timeout: Int) : CustomExpiry<Long, Customer> {

    override fun getExpiry(entry: Region.Entry<Long, Customer>) =
            if (entry.key % 3 == 0L) {
                ExpirationAttributes(timeout, ExpirationAction.DESTROY)
            } else {
                null
            }
}
