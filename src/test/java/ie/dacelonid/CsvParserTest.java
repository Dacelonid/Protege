package ie.dacelonid;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CsvParserTest {

    private static final int NUMBER_LINES_CSV = 10;
    private CsvParser objUnderTest;

    @Before
    public void setup() {
        objUnderTest = new CsvParser();
    }

    @Test
    public void getAllLines_multiLine_getCorrectValues() {
        final String testCSV = "test1\ttest2\ttest3";
        final List<String> inputs = getInputLines(testCSV);

        final List<List<String>> expectedValues = getExpectedValues(new ListValueConverter(), testCSV);

        objUnderTest.parseCsv(inputs);
        final List<List<String>> actualValues = objUnderTest.getAllLines();

        assertThat(actualValues, is(expectedValues));
    }

    @Test
    public void getAnnotations_multiLineOneAnnotation_getExpectedAnnotations() {
        final String testCSV = "test1\ttest2\ttest3";
        final List<String> inputs = getInputLines(testCSV);

        final List<String> expectedValues = getExpectedValues(new AnnotationValueConverter(), "test3");

        objUnderTest.parseCsv(inputs);
        final List<String> actualValues = objUnderTest.getAnnotations();

        assertThat(actualValues, is(expectedValues));
    }

    private List<String> getInputLines(final String testCSV) {
        final List<String> inputs = new ArrayList<>();
        for (int x = 0; x < NUMBER_LINES_CSV; x++) {
            inputs.add(testCSV);
        }
        return inputs;
    }

    private <T> List<T> getExpectedValues(final ExpectedValuesConverter<T> converter, final String value) {
        final List<T> expectedValues = new ArrayList<>();
        for (int x = 0; x < NUMBER_LINES_CSV; x++) {
            expectedValues.add(converter.getExpectedValues(value));
        }
        return expectedValues;
    }
}
