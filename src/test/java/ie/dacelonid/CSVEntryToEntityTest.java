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
        List<CSVEntries> itemsToConvert = getCsvEntries(3);
        assertEquals(expectedEntities, objUnderTest.convert(itemsToConvert));
    }

    private List<CSVEntries> getCsvEntries(int numberOfEntries) {
        List<CSVEntries> entries = new ArrayList<>();
        for (int x = 1; x <= numberOfEntries; x++) {
            entries.add(new CSVEntries("Description" + x, Collections.singletonList("Annotation" + x)));
        }
        return entries;
    }

    @Test
    public void convert_CSVEntriesWithSubClasses_getEntities() {
        Entity topLevelEntity = new Entity("Flag");
        Entity subClassEntity = new Entity(topLevelEntity, "Country_Flag");
        List<Entity> expectedEntity = Arrays.asList(topLevelEntity, subClassEntity);
        List<CSVEntries> itemsToConvert = Arrays.asList(new CSVEntries("Flag", Collections.singletonList("flag")), new CSVEntries("Country_Flag", Arrays.asList("flag", "other")));
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        assertEquals(expectedEntity, objUnderTest.convert(itemsToConvert));
    }
}