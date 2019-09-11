package examples.springdata.geode.server.expiration.custom.expiration;

import examples.springdata.geode.domain.Customer;
import org.apache.geode.cache.CustomExpiry;
import org.apache.geode.cache.ExpirationAction;
import org.apache.geode.cache.ExpirationAttributes;
import org.apache.geode.cache.Region;


public class CustomCustomerExpiry implements CustomExpiry<Long, Customer> {

    private int timeout;

    public CustomCustomerExpiry(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public ExpirationAttributes getExpiry(Region.Entry<Long, Customer> entry) {
        if (entry.getKey() % 3 == 0) {
            return new ExpirationAttributes(timeout, ExpirationAction.DESTROY);
        }
        return null;
    }
}