package examples.springdata.geode.server.lucene.domain;

import examples.springdata.geode.domain.EmailAddress;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.LuceneIndexed;
import org.springframework.data.gemfire.mapping.annotation.PartitionRegion;

@PartitionRegion(name = "Customers")
public class JavaLuceneCustomer {
    @Id
    private Long id;

    private EmailAddress emailAddress;

    private String firstName;

    @LuceneIndexed(name = "lastName_lucene")
    private String lastName;

    public JavaLuceneCustomer(long id, EmailAddress emailAddress, String firstName, String lastName) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
