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

public class MainClassTest {

    @Test
    public void generateOwlFile_compareWithReference() throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        String expectedContents = readFile("Gold.owl");
        MainClass.main(new String[]{});
        String actualContents = readFile("filename.owl");
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
        Path pathToFile = getPathToFile("filename.owl");
        if (pathToFile != null)
            pathToFile.toFile().delete();
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