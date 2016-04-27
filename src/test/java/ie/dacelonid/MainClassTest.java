package ie.dacelonid;

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

import static ie.dacelonid.MainClass.DEFAULT_OUTPUT_FILENAME;

public class MainClassTest {

    private String expectedContents;

    @Before
    public void setup() throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        expectedContents = readFile("Gold.owl");
    }

    @Test
    public void generateOwlFile_compareWithReference() throws Exception {
        MainClass.main(new String[]{});
        String actualContents = readFile(DEFAULT_OUTPUT_FILENAME);
        XMLAssert.assertXMLEqual(expectedContents, actualContents);
    }

    @Test
    public void generateOwlFile_supplyingFilename_compareWithReference() throws Exception {
        MainClass.main(new String[]{"non_default_filename.owl", "Emoji_Unicodes.csv"});
        String actualContents = readFile("non_default_filename.owl");
        XMLAssert.assertXMLEqual(expectedContents, actualContents);
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
        Path pathToFile = getPathToFile(DEFAULT_OUTPUT_FILENAME);
        if (pathToFile != null)
            if (!pathToFile.toFile().delete()) {
                throw new Exception("Failed to delete " + DEFAULT_OUTPUT_FILENAME);
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