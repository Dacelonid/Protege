package ie.dacelonid;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CsvParserFunctionalTest {

    @Test
    public void readValidEmojiCSV_getListOfAnnotations() throws Exception {
        final CsvParser objUnderTest = CsvParserFactory.createCsvParser();
    }

    @Test
    public void findFlags() throws Exception {
        final CsvParser objUnderTest = CsvParserFactory.createCsvParser();

        assertEquals(10, objUnderTest.getSubClass(TopLevelClass.FLAG).size());
    }


    @Test
    public void annotationsAreSortedByType() throws Exception {
        final CsvParser objUnderTest = CsvParserFactory.createCsvParser();
        List<String> expectedValues = Arrays.asList("flag for China", "flag for Germany", "flag for Spain", "flag for France",
                "flag for United Kingdom", "flag for Italy", "flag for Japan", "flag for South Korea", "flag for Russia", "flag for United States");
        assertEquals(expectedValues, objUnderTest.getTopLevelClasses().get(TopLevelClass.FLAG));
    }
}
