package examples.springdata.geode.server.lucene.domain;

import examples.springdata.geode.domain.EmailAddress;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.LuceneIndexed;
import org.springframework.data.gemfire.mapping.annotation.PartitionRegion;

import java.io.Serializable;

@PartitionRegion(name = "Customers")
public class Customer implements Serializable {
    @Id
    private Long id;

    private EmailAddress emailAddress;

    private String firstName;

    @LuceneIndexed(name = "lastName_lucene")
    private String lastName;

    public Customer(long id, EmailAddress emailAddress, String firstName, String lastName) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Customer(id=" + id + ", emailAddress=" + emailAddress +", firstName=" + firstName + ", lastName=" + lastName + ")";
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}