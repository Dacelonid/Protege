package ie.dacelonid;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class OwlTest {

    @Test
    public void toString_blankOntology_shouldBeValidEmptyOwlFile() throws Exception{
        Owl objUnderTest = new Owl();
        String expectedOwl = readFile("blankOntology.owl");
        String resultOwl = objUnderTest.toString();
        XMLUnit.setIgnoreWhitespace(true);

        XMLAssert.assertXMLEqual(resultOwl, expectedOwl);
    }

    private String readFile(String fileName) throws Exception {
        Path pathToFile = Paths.get(this.getClass().getClassLoader().getResource(fileName).toURI());
        final String fileContentsAsString = new String(Files.readAllBytes(pathToFile));
        return fileContentsAsString;
    }
}