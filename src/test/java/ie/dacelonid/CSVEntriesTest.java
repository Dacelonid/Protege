package ie.dacelonid;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class CSVEntriesTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(CSVEntries.class).verify();
    }
}