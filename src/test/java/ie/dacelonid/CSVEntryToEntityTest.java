package ie.dacelonid;

import ie.dacelonid.CSVParser.CSVEntry;
import ie.dacelonid.CSVParser.CSVEntryToEntity;
import ie.dacelonid.ontology.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVEntryToEntityTest {

    private Thing annotation;
    private Thing classes;
    private List<RdfElement> expectedEntities;

    @Before
    public void setup() {
        annotation = new Annotation("annotations");
        classes = new Entity("classes");
        expectedEntities = new ArrayList<>();
        expectedEntities.add(annotation);
        expectedEntities.add(classes);
        expectedEntities.add(new DisjointClasses("#annotations", "#classes"));
        Property property = new Property.PropertyBuilder().name("has_annotation").functional().domain(new Entity("classes")).range(new Annotation("annotations")).build();
        expectedEntities.add(property);
    }

    @Test
    public void convert_multipleCsvEntries_getEntitiesIncludingSubclasses() {
        Thing face = new Entity(classes, "Face", Collections.singletonList("Face"));
        Thing smilingFace = new Entity(face, "Smiling Face", Arrays.asList("Face", "Eyes", "Smile"));
        Thing smilingFaceWithEyes = new Entity(smilingFace, "Smiling_Face_with_Eyes", Arrays.asList("Face", "Eyes", "Smile"));
        Thing face_annotation = new Annotation(annotation, "Face");
        Thing eyes_annotation = new Annotation(annotation, "Eyes");
        Thing smile_annotation = new Annotation(annotation, "Smile");
        expectedEntities.addAll(Arrays.asList(face_annotation, eyes_annotation, smile_annotation, face, smilingFace, smilingFaceWithEyes));
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntry> itemsToConvert = Collections.singletonList(new CSVEntry("Smiling Face with Eyes", "emoji", Arrays.asList("Face", "Eyes", "Smile")));
        objUnderTest.convert(itemsToConvert);
        assertEquals(expectedEntities, objUnderTest.getProperties());
    }

    @Test
    public void convert_wordsThatShouldntBeClasses_ideograph_shouldIgnoreThoseWords() {
        Thing topLevel = new Entity("classes");
        Thing parent = new Entity(topLevel, "ideograph", Collections.emptyList());
        Thing ideograph6708 = new Entity(parent, "squared cjk unified ideograph-6708", Arrays.asList("japanese", "symbol", "word"));
        Thing japanese_annotation = new Annotation(annotation, "japanese");
        Thing symbol_annotation = new Annotation(annotation, "symbol");
        Thing word_annotation = new Annotation(annotation, "word");

        expectedEntities.addAll(Arrays.asList(japanese_annotation, symbol_annotation, word_annotation, parent, ideograph6708));
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntry> itemsToConvert = Collections.singletonList(new CSVEntry("squared cjk unified ideograph-6708", "emoji", Arrays.asList("japanese", "symbol", "word")));

        objUnderTest.convert(itemsToConvert);
        assertEquals(expectedEntities, objUnderTest.getProperties());
    }

    @Test
    public void convert_wordsThatShouldntBeClasses_letter_shouldIgnoreThoseWords() {
        Thing parent = new Entity("text");
        Thing ideograph6708 = new Entity(parent, "negative squared latin capital letter a", Arrays.asList("a", "blood", "symbol", "word"));
        Thing a_annotation = new Annotation(annotation, "a");
        Thing blood_annotation = new Annotation(annotation, "blood");
        Thing symbol_annotation = new Annotation(annotation, "symbol");
        Thing word_annotation = new Annotation(annotation, "word");

        expectedEntities.addAll(Arrays.asList(a_annotation, blood_annotation, symbol_annotation, word_annotation, parent, ideograph6708));
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntry> itemsToConvert = Collections.singletonList(new CSVEntry("negative squared latin capital letter a", "text", Arrays.asList("a", "blood", "symbol", "word")));

        objUnderTest.convert(itemsToConvert);
        assertEquals(expectedEntities, objUnderTest.getProperties());
    }

    @Test
    public void convert_multipleCSVEntriesWithMultipleClasses_shouldIgnoreAdjectives() {
        Thing face = new Entity(classes, "Face", Collections.singletonList("Face"));
        Thing smilingFace = new Entity(face, "Smiling Face", Arrays.asList("Face", "Smile"));
        Thing face_annotation = new Annotation(annotation, "Face");
        Thing smile_annotation = new Annotation(annotation, "Smile");

        expectedEntities.addAll(Arrays.asList(face_annotation, smile_annotation, face, smilingFace));
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntry> itemsToConvert = Collections.singletonList(new CSVEntry("Smiling Face", "emoji", Arrays.asList("Face", "Smile")));

        objUnderTest.convert(itemsToConvert);
        assertEquals(expectedEntities, objUnderTest.getProperties());
    }

    @Test
    public void convert_annotationsWithSpaces_shouldConvertToUnderscores() {
        Thing face = new Entity(classes, "Face", Collections.singletonList("Smiling face"));
        Thing smilingFace = new Entity(face, "Smiling Face", Collections.singletonList("Smiling face"));
        Thing smile_annotation = new Annotation(annotation, "Smiling_face");

        expectedEntities.addAll(Arrays.asList(smile_annotation, face, smilingFace));
        CSVEntryToEntity objUnderTest = new CSVEntryToEntity();
        List<CSVEntry> itemsToConvert = Collections.singletonList(new CSVEntry("Smiling Face", "emoji", Collections.singletonList("Smiling face")));

        objUnderTest.convert(itemsToConvert);
        List<RdfElement> properties = objUnderTest.getProperties();
        assertEquals(expectedEntities, properties);
    }

}