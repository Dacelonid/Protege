package ie.dacelonid;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVEntryToEntityTest {
    @Test
    public void convert_multipleCsvEntries_getEntitiesIncludingSubclasses() {
        Entity face = new Entity("Face");
        Entity smilingFace = new Entity(face, "Smiling Face");
        Entity smilingFaceWithEyes = new Entity(smilingFace, "Smiling_Face_with_Eyes");

        List<Entity> expectedEntities = Arrays.asList(face, smilingFace, smilingFaceWithEyes);
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntry> itemsToConvert = Collections.singletonList(
                new CSVEntry("Smiling Face with Eyes", "emoji", Arrays.asList("Face", "Eyes", "Smile")));
        assertEquals(expectedEntities, objUnderTest.convert(itemsToConvert));
    }

    @Test
    public void convert_wordsThatShouldntBeClasses_ideograph_shouldIgnoreThoseWords() {
        Entity parent = new Entity("ideograph");
        Entity ideograph6708 = new Entity(parent, "squared cjk unified ideograph-6708");

        List<Entity> expectedEntities = Arrays.asList(parent, ideograph6708);
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntry> itemsToConvert = Collections.singletonList(
                new CSVEntry("squared cjk unified ideograph-6708", "emoji", Arrays.asList("japanese", "symbol", "word")));
        assertEquals(expectedEntities, objUnderTest.convert(itemsToConvert));
    }

    @Test
    public void convert_wordsThatShouldntBeClasses_ideograph_shouldIgnoreThoseWords2() {
        Entity parent = new Entity("ideograph");
        Entity ideograph6708 = new Entity(parent, "squared cjk unified ideograph-7121");

        List<Entity> expectedEntities = Arrays.asList(parent, ideograph6708);
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntry> itemsToConvert = Collections.singletonList(
                new CSVEntry("squared cjk unified ideograph-7121", "emoji", Arrays.asList("japanese", "symbol", "word")));
        assertEquals(expectedEntities, objUnderTest.convert(itemsToConvert));
    }

    @Test
    public void convert_wordsThatShouldntBeClasses_letter_shouldIgnoreThoseWords() {
        Entity parent = new Entity("text");
        Entity ideograph6708 = new Entity(parent, "negative squared latin capital letter a");

        List<Entity> expectedEntities = Arrays.asList(parent, ideograph6708);
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntry> itemsToConvert = Collections.singletonList(
                new CSVEntry("negative squared latin capital letter a", "text", Arrays.asList("a", "blood", "symbol", "word")));
        assertEquals(expectedEntities, objUnderTest.convert(itemsToConvert));
    }

    @Test
    public void convert_multipleCSVEntriesWithMultipleClasses_shouldIgnoreAdjectives() {
        Entity face = new Entity("Face");
        Entity smilingFace = new Entity(face, "Smiling Face");

        List<Entity> expectedEntities = Arrays.asList(face, smilingFace);
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntry> itemsToConvert = Collections.singletonList(new CSVEntry("Smiling Face", "emoji", Arrays.asList("Face", "Smile")));
        assertEquals(expectedEntities, objUnderTest.convert(itemsToConvert));
    }

}