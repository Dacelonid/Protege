package ie.dacelonid;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class OwlTest {
    private Owl objUnderTest;

    @Before
    public void setup() {
        objUnderTest = new Owl();
        XMLUnit.setIgnoreWhitespace(true);
    }

    @Test
    public void toString_blankOntology_shouldBeValidEmptyOwlFile() throws Exception {

        String expectedOwl = readFile("blankOntology.owl");
        String resultOwl = objUnderTest.toString();

        XMLAssert.assertXMLEqual(resultOwl, expectedOwl);
    }

    @Test
    public void toString_ontologyWithOneEntity_shouldBeValidOwlFile() throws Exception {
        objUnderTest.addEntity(new Entity("Emoji"));

        String expectedOwl = readFile("ontologyWithOneEntity.owl");
        String resultOwl = objUnderTest.toString();

        XMLAssert.assertXMLEqual(resultOwl, expectedOwl);
    }

    @Test
    public void toString_ontologyWithMultipleTopLevelEntities_shouldBeValidOwlFile() throws Exception {
        objUnderTest.addEntity(new Entity("Emoji"));
        objUnderTest.addEntity(new Entity("Text"));
        objUnderTest.addEntity(new Entity("Other"));

        String expectedOwl = readFile("ontologyWithMultipleTopLevelEntities.owl");
        String resultOwl = objUnderTest.toString();

        XMLAssert.assertXMLEqual(resultOwl, expectedOwl);
    }

    @Test
    public void toString_ontologyWithSubclasses_shouldBeValidOwlFile() throws Exception {
        Entity parent = new Entity("Emoji");
        Entity child = new Entity(parent, "AnimalEmoji");
        objUnderTest.addEntity(parent);
        objUnderTest.addEntity(child);
        String expectedOwl = readFile("ontologyWithSubclasses.owl");
        String resultOwl = objUnderTest.toString();

        XMLAssert.assertXMLEqual(resultOwl, expectedOwl);
    }
    @Test
    public void toString_ontologyWithSubclassesOfSubclasses_shouldBeValidOwlFile() throws Exception {
        Entity parent = new Entity("Emoji");
        Entity child = new Entity(parent, "AnimalEmoji");
        Entity subChild = new Entity(child, "DogEmoji");
        objUnderTest.addEntity(parent);
        objUnderTest.addEntity(child);
        objUnderTest.addEntity(subChild);
        String expectedOwl = readFile("ontologyWithSubclassesOfSubclasses.owl");
        String resultOwl = objUnderTest.toString();

        XMLAssert.assertXMLEqual(resultOwl, expectedOwl);
    }

    @Test
    public void toString_ontologyWithAllEmojisAsTopLevelEntities_shouldBeValidOwlFile() throws Exception{
        CsvParser parser = CsvParserFactory.createCsvParser();
        for(String description : parser.getDescriptions()){
            objUnderTest.addEntity(new Entity(description));
        }
        String expectedOwl = readFile("ontologyWithAllEmojisAsTopLevelEntities.owl");
        String resultOwl = objUnderTest.toString();
        XMLAssert.assertXMLEqual(resultOwl, expectedOwl);
    }

    private String readFile(String fileName) throws Exception {
        Path pathToFile = Paths.get(this.getClass().getClassLoader().getResource(fileName).toURI());
        final String fileContentsAsString = new String(Files.readAllBytes(pathToFile));
        return fileContentsAsString;
    }


}