package ie.dacelonid;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class CsvParserFunctionalTest {

    @Test
    public void readValidEmojiCSV_getListOfAnnotations() throws Exception {
        final CsvParser objUnderTest = getCsvParser();
        System.out.println(objUnderTest.getAnnotations());
        System.out.println(objUnderTest.getDescriptions());
    }

    @Test
    public void findFlags() throws Exception {
        final CsvParser objUnderTest = getCsvParser();

        assertEquals(10, objUnderTest.getSubClass(TopLevelClass.FLAG).size());
    }

    private CsvParser getCsvParser() throws IOException {
        Optional<URL> url = Optional.ofNullable(Thread.currentThread().getContextClassLoader().getResource("Emoji_Unicodes.csv"));
        final File file = new File(url.get().getPath());

        final List<String> list = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final CsvParser objUnderTest = new CsvParser();
        objUnderTest.parseCsv(list);
        return objUnderTest;
    }

    @Test
    public void annotationsAreSortedByType() throws Exception {
        final CsvParser objUnderTest = getCsvParser();
        List<String> expectedValues = Arrays.asList("flag for China", "flag for Germany", "flag for Spain", "flag for France",
                "flag for United Kingdom", "flag for Italy", "flag for Japan", "flag for South Korea", "flag for Russia", "flag for United States");
        assertEquals(expectedValues, objUnderTest.getTopLevelClasses().get(TopLevelClass.FLAG));
    }
}
