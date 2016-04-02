package ie.dacelonid;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVEntryToEntityTest {

    @Test
    public void convert_multipleCSVEntries_getEntities() {
        List<Entity> expectedEntities = Arrays.asList(new Entity("Description1"), new Entity("Description2"), new Entity("Description3"));
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntry> itemsToConvert = getCsvEntries(3);
        assertEquals(expectedEntities, objUnderTest.convert(itemsToConvert));
    }

    private List<CSVEntry> getCsvEntries(int numberOfEntries) {
        List<CSVEntry> entries = new ArrayList<>();
        for (int x = 1; x <= numberOfEntries; x++) {
            entries.add(new CSVEntry("Description" + x, Collections.singletonList("Annotation" + x)));
        }
        return entries;
    }

    @Test
    public void convert_CSVEntriesWithSubClasses_getEntities() {
        Entity topLevelEntity = new Entity("Flag");
        Entity subClassEntity = new Entity(topLevelEntity, "Flag for Country");
        List<Entity> expectedEntity = Arrays.asList(topLevelEntity, subClassEntity);
        List<CSVEntry> itemsToConvert = Arrays.asList(new CSVEntry("Flag", Collections.singletonList("flag")), new CSVEntry("Flag for Country", Arrays.asList("flag", "other")));
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        assertEquals(expectedEntity, objUnderTest.convert(itemsToConvert));
    }
}