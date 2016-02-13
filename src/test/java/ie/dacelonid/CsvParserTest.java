package ie.dacelonid;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
public class CsvParserTest {

    private static final String COLUMN_HEADINGS = "Description\tNature\tAnnotations";
    private static final String JUMBLED_COLUMN_HEADINGS = "Nature\tAnnotations\tDescription";
    private static final int NUMBER_LINES_CSV = 10;
    private CsvParser objUnderTest;

    @Before
    public void setup() {
        objUnderTest = new CsvParser();
    }

    @Test
    public void getAllLines_multiLineInput_getCorrectValues() {
        final String testCSV = "test1\ttest2\ttest3";
        final List<String> inputs = getInputLines(testCSV, COLUMN_HEADINGS);

        final List<List<String>> expectedValues = getExpectedValues(new ListValueConverter(), testCSV);

        objUnderTest.parseCsv(inputs);
        final List<List<String>> actualValues = objUnderTest.getAllLines();

        assertThat(actualValues, is(expectedValues));
    }

    @Test
    public void getAnnotations_multiLineInput_getExpectedAnnotations() {
        final String testCSV = "test1\ttest2\ttest3";
        final List<String> inputs = getInputLines(testCSV, COLUMN_HEADINGS);

        final List<String> expectedValues = Collections.singletonList("test3");

        objUnderTest.parseCsv(inputs);
        final List<String> actualValues = objUnderTest.getAnnotations();

        assertThat(actualValues, is(expectedValues));
    }

    @Test
    public void getAnnotations_multiLineInput_getExpectedAnnotationsNoDuplicates() {
        final String testCSV = "test1\ttest2\ttest3a,test3b,test3c";
        final List<String> inputs = getInputLines(testCSV, COLUMN_HEADINGS);

        final List<String> expectedValues = Arrays.asList("test3a", "test3b", "test3c");

        objUnderTest.parseCsv(inputs);
        final List<String> actualValues = objUnderTest.getAnnotations();

        assertThat(actualValues, is(expectedValues));
    }

    @Test
    public void getDescription_multiLineInput_getExpectedDescription() {
        final String testCSV = "test1\ttest2\ttest3";
        final List<String> inputs = getInputLines(testCSV, COLUMN_HEADINGS);

        final List<String> expectedValues = getExpectedValues(new StringValueConverter(), "test1");

        objUnderTest.parseCsv(inputs);
        final List<String> actualValues = objUnderTest.getDescriptions();

        assertThat(actualValues, is(expectedValues));
    }

    @Test
    public void getAnnotation_annotationIsNotThirdColumn_shoudlGetAnnotations() {
        final String testCSV = "test1\ttest3a,test3b,test3c\ttest2";
        final List<String> inputs = getInputLines(testCSV, JUMBLED_COLUMN_HEADINGS);

        final List<String> expectedValues = Arrays.asList("test3a", "test3b", "test3c");

        objUnderTest.parseCsv(inputs);
        final List<String> actualValues = objUnderTest.getAnnotations();

        assertThat(actualValues, is(expectedValues));
    }

    @Test
    public void getAnnotations_annotationsWithSpaces_expectSpacesToBeIgnored() throws Exception {
        final String testCSV = "test1\ttest3a,test3b, test3c,   test3d\ttest2";
        final List<String> inputs = getInputLines(testCSV, JUMBLED_COLUMN_HEADINGS);

        final List<String> expectedValues = Arrays.asList("test3a", "test3b", "test3c", "test3d");

        objUnderTest.parseCsv(inputs);
        final List<String> actualValues = objUnderTest.getAnnotations();

        assertThat(actualValues, is(expectedValues));

    }

    @Test
    public void getAnnotationMap_multiLineInput_getCorrectValues() {
        final List<String> inputs = getInputLines("Description1\tNature1\tAnnotation1a,Annotation1b,Annotation1c,Annotation1d", COLUMN_HEADINGS);
        inputs.addAll(createInputLines("Description2\tNature2\tAnnotation1a,Annotation2b,Annotation2c,Annotation2d"));
        inputs.addAll(createInputLines("Description3\tNature3\tAnnotation1a,Annotation2b,Annotation3c"));
        inputs.addAll(createInputLines("Description4\tNature4\tAnnotation4a,Annotation4b,Annotation4c,Annotation4d"));
        inputs.addAll(createInputLines("Description5\tNature5\tAnnotation5a,Annotation5b"));
        inputs.addAll(createInputLines("Description6\tNature6\tAnnotation1a,Annotation2a,Annotation3a,Annotation4a,Annotation5a"));

        objUnderTest.parseCsv(inputs);
        List<String> expectedValues = Arrays.asList("Description1", "Description2", "Description3", "Description6");
        List<String> actualValues = objUnderTest.getEmojiContainingAnnotation("Annotation1a");

        assertTrue(expectedValues.containsAll(actualValues));
        assertTrue(actualValues.containsAll(expectedValues));
    }

    private List<String> getInputLines(final String testCSV, final String columnNames) {
        final List<String> inputs = new ArrayList<>();
        inputs.add(columnNames);
        inputs.addAll(createInputLines(testCSV));
        return inputs;
    }

    private List<String> createInputLines(String testCSV) {
        List<String> inputs = new ArrayList<>();
        for (int x = 0; x < NUMBER_LINES_CSV; x++) {
            inputs.add(testCSV);
        }
        return inputs;
    }

    private <T> List<T> getExpectedValues(final ExpectedValuesConverter<T> converter, final String... values) {
        final List<T> expectedValues = new ArrayList<>();
        for (int x = 0; x < NUMBER_LINES_CSV; x++) {
            for (final String value : values) {
                expectedValues.add(converter.getExpectedValues(value));
            }
        }
        return expectedValues;
    }
}
