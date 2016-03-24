package ie.dacelonid;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVEntryToEntityTest {

    @Test
    public void convert_oneCSVEntry_getEntity() {
        Entity expectedResult = new Entity("Description1");
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        CSVEntries itemToConvert = new CSVEntries("Description1", Collections.singletonList("Annotation1"));
        assertEquals(expectedResult, objUnderTest.convert(itemToConvert));
    }

    @Test
    public void convert_multipleCSVEntries_getEntities() {
        List<Entity> expectedEntities = Arrays.asList(new Entity("Description1"), new Entity("Description2"), new Entity("Description3"));
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntries> itemsToConvert = Arrays.asList(new CSVEntries("Description1", Collections.singletonList("Annotation1")), new CSVEntries("Description2", Collections.singletonList("Annotation2")), new CSVEntries("Description3", Collections.singletonList("Annotation3")));
        assertEquals(expectedEntities, objUnderTest.convert(itemsToConvert));
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