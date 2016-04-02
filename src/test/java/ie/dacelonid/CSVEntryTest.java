package ie.dacelonid;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class CSVEntryTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(CSVEntry.class).verify();
    }
}