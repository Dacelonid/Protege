package ie.dacelonid;

import ie.dacelonid.ontology.Annotation;
import ie.dacelonid.ontology.Entity;
import ie.dacelonid.ontology.Thing;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class ThingTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Thing.class).withPrefabValues(Thing.class, new Entity("entity"), new Annotation("annotation")).verify();
    }
}