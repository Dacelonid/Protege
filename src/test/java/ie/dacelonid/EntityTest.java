package ie.dacelonid;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class EntityTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Entity.class).withPrefabValues(Entity.class, new Entity("Blah"), new Entity("foo")).verify();
    }
}