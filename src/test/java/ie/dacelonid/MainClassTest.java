package ie.dacelonid;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.fail;

public class MainClassTest {

    private static final String DEFAULT_FILENAME = "filename.owl";
    private String expectedContents;

    @Before
    public void setup() throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        expectedContents = readFile("Gold.owl");
    }

    @Test
    public void generateOwlFile_compareWithReference() throws Exception {
        MainClass.main(new String[]{});
        String actualContents = readFile(DEFAULT_FILENAME);
        XMLAssert.assertXMLEqual(actualContents, expectedContents);
    }

    @Test
    public void generateOwlFile_compareWithReferenceTemp() throws Exception {
        MainClass.main(new String[]{"temp_output.owl", "temp.csv"});
        String actualContents = readFile("temp_output.owl");
        String test = readFile("temp-gold.owl");
        Diff diff = XMLUnit.compareXML(actualContents, test);
        if (!diff.similar()) {
            DetailedDiff dd = new DetailedDiff(diff);
            System.out.println(dd.getAllDifferences());
            fail();
        }

//        XMLAssert.assertXMLEqual(actualContents, test);
    }

    @Test
    public void generateOwlFile_supplyingFilename_compareWithReference() throws Exception {
        MainClass.main(new String[]{"non_default_filename.owl", "Emoji_Unicodes.csv"});
        String actualContents = readFile("non_default_filename.owl");
        XMLAssert.assertXMLEqual(actualContents, expectedContents);
    }


    private String readFile(String fileName) throws Exception {
        Path pathToFile = getPathToFile(fileName);
        if (pathToFile != null) {
            return new String(Files.readAllBytes(pathToFile));
        }
        throw new IllegalArgumentException("Could not find " + fileName);
    }

    @Before
    @After
    public void deleteFile() throws Exception {
        Path pathToFile = getPathToFile(DEFAULT_FILENAME);
        if (pathToFile != null) if (!pathToFile.toFile().delete()) {
            throw new Exception("Failed to delete " + DEFAULT_FILENAME);
        }
    }

    private Path getPathToFile(String fileName) throws URISyntaxException {
        URL resource = this.getClass().getClassLoader().getResource(fileName);
        if (resource != null) {
            return Paths.get(resource.toURI());
        }
        File file = new File(fileName);
        if (file.exists()) {
            return file.toPath();
        }
        return null;
    }
}