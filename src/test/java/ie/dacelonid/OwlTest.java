package ie.dacelonid;

import ie.dacelonid.CSVParser.CsvParser;
import ie.dacelonid.CSVParser.CsvParserFactory;
import ie.dacelonid.ontology.Entity;
import ie.dacelonid.ontology.Ontology;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class OwlTest {
    private Ontology objUnderTest;

    @Before
    public void setup() {
        objUnderTest = new Ontology();
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
    public void toString_ontologyWithAllEmojisAsTopLevelEntities_shouldBeValidOwlFile() throws Exception {
        CsvParser parser = CsvParserFactory.createCsvParser("Emoji_Unicodes.csv");
        for (String description : parser.getDescriptions()) {
            objUnderTest.addEntity(new Entity(description));
        }
        String expectedOwl = readFile("ontologyWithAllEmojisAsTopLevelEntities.owl");
        String resultOwl = objUnderTest.toString();
        XMLAssert.assertXMLEqual(resultOwl, expectedOwl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createCsvParser_unknownFile_shouldThrowException() throws Exception {
        CsvParser parser = CsvParserFactory.createCsvParser("non_existing_file.csv");
    }

    private String readFile(String fileName) throws Exception {
        URL resource = this.getClass().getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("Could not find file " + fileName);
        }
        Path pathToFile = Paths.get(resource.toURI());
        return new String(Files.readAllBytes(pathToFile));
    }


}