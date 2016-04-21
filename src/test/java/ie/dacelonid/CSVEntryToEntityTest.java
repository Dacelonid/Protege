package ie.dacelonid;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVEntryToEntityTest {

    private Entity annotation;
    private Entity classes;
    private List<RdfElement> expectedEntities;

    @Before
    public void setup() {
        annotation = new Entity("annotations");
        classes = new Entity("classes");
        expectedEntities = new ArrayList<>();
        expectedEntities.add(annotation);
        expectedEntities.add(classes);
        expectedEntities.add(new DisjointClasses("#annotations", "#classes"));
        Property property = new Property.PropertyBuilder().name("has_annotation").functional().domain(new Entity("classes")).range(
                new Entity("annotations")).build();
        expectedEntities.add(property);
    }

    @Test
    public void convert_multipleCsvEntries_getEntitiesIncludingSubclasses() {
        Entity face = new Entity(classes, "Face", Collections.singletonList("Face"));
        Entity smilingFace = new Entity(face, "Smiling Face", Arrays.asList("Face", "Eyes", "Smile"));
        Entity smilingFaceWithEyes = new Entity(smilingFace, "Smiling_Face_with_Eyes", Arrays.asList("Face", "Eyes", "Smile"));
        Entity face_annotation = new Entity(annotation, "Face_annotation");
        Entity eyes_annotation = new Entity(annotation, "Eyes_annotation");
        Entity smile_annotation = new Entity(annotation, "Smile_annotation");
        expectedEntities.addAll(Arrays.asList(face_annotation, eyes_annotation, smile_annotation, face, smilingFace, smilingFaceWithEyes));
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntry> itemsToConvert = Collections.singletonList(
                new CSVEntry("Smiling Face with Eyes", "emoji", Arrays.asList("Face", "Eyes", "Smile")));
        objUnderTest.convert(itemsToConvert);
        assertEquals(expectedEntities, objUnderTest.getProperties());
    }

    @Test
    public void convert_wordsThatShouldntBeClasses_ideograph_shouldIgnoreThoseWords() {
        Entity parent = new Entity("ideograph");
        Entity ideograph6708 = new Entity(parent, "squared cjk unified ideograph-6708", Arrays.asList("japanese", "symbol", "word"));
        Entity japanese_annotation = new Entity(annotation, "japanese_annotation");
        Entity symbol_annotation = new Entity(annotation, "symbol_annotation");
        Entity word_annotation = new Entity(annotation, "word_annotation");

        expectedEntities.addAll(Arrays.asList(japanese_annotation, symbol_annotation, word_annotation, parent, ideograph6708));
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntry> itemsToConvert = Collections.singletonList(
                new CSVEntry("squared cjk unified ideograph-6708", "emoji", Arrays.asList("japanese", "symbol", "word")));

        objUnderTest.convert(itemsToConvert);
        assertEquals(expectedEntities, objUnderTest.getProperties());
    }

    @Test
    public void convert_wordsThatShouldntBeClasses_letter_shouldIgnoreThoseWords() {
        Entity parent = new Entity("text");
        Entity ideograph6708 = new Entity(parent, "negative squared latin capital letter a", Arrays.asList("a", "blood", "symbol", "word"));
        Entity a_annotation = new Entity(annotation, "a_annotation");
        Entity blood_annotation = new Entity(annotation, "blood_annotation");
        Entity symbol_annotation = new Entity(annotation, "symbol_annotation");
        Entity word_annotation = new Entity(annotation, "word_annotation");

        expectedEntities.addAll(Arrays.asList(a_annotation, blood_annotation, symbol_annotation, word_annotation, parent, ideograph6708));
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntry> itemsToConvert = Collections.singletonList(
                new CSVEntry("negative squared latin capital letter a", "text", Arrays.asList("a", "blood", "symbol", "word")));

        objUnderTest.convert(itemsToConvert);
        assertEquals(expectedEntities, objUnderTest.getProperties());
    }

    @Test
    public void convert_multipleCSVEntriesWithMultipleClasses_shouldIgnoreAdjectives() {
        Entity face = new Entity(classes, "Face", Collections.singletonList("Face"));
        Entity smilingFace = new Entity(face, "Smiling Face", Arrays.asList("Face", "Smile"));
        Entity face_annotation = new Entity(annotation, "Face_annotation");
        Entity smile_annotation = new Entity(annotation, "Smile_annotation");

        expectedEntities.addAll(Arrays.asList(face_annotation, smile_annotation, face, smilingFace));
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntry> itemsToConvert = Collections.singletonList(new CSVEntry("Smiling Face", "emoji", Arrays.asList("Face", "Smile")));

        objUnderTest.convert(itemsToConvert);
        assertEquals(expectedEntities, objUnderTest.getProperties());
    }

    @Test
    public void convert_annotationsWithSpaces_shouldConvertToUnderscores() {
        Entity face = new Entity(classes, "Face", Collections.singletonList("Smiling face"));
        Entity smilingFace = new Entity(face, "Smiling Face", Collections.singletonList("Smiling face"));
        Entity smile_annotation = new Entity(annotation, "Smiling_face_annotation");

        expectedEntities.addAll(Arrays.asList(smile_annotation, face, smilingFace));
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntry> itemsToConvert = Collections.singletonList(new CSVEntry("Smiling Face", "emoji", Collections.singletonList("Smiling face")));

        objUnderTest.convert(itemsToConvert);
        List<RdfElement> properties = objUnderTest.getProperties();
        assertEquals(expectedEntities, properties);
    }

}