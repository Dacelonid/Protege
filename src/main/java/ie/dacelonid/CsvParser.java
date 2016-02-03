package ie.dacelonid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvParser {
    private static final int ANNOTATION_COLUMN = 2;
    private static final int DESCRIPTION_COLUMN = 1;

    private static final String COLUMN_DELIMITER = "\t";
    private static final String COLUMN_FIELD_REGEX = "\\s*" + COLUMN_DELIMITER + "\\s*";

    private static final String ENTRY_DELIMITER = ",";
    private static final String ENTRY_FIELD_REGEX = "\\s*" + ENTRY_DELIMITER + "\\s*";

    private final List<List<String>> allLines = new ArrayList<>();
    private final List<String> annotations = new ArrayList<>(); // @TODO should be a Set but then I need to sort it
    private final List<String> descriptions = new ArrayList<>();

    public void parseCsv(final List<String> inputData) {
        for (final String line : inputData) {
            processLine(line.split(COLUMN_FIELD_REGEX));
        }
    }

    private void processLine(final String[] delimitedString) {
        populateAnnotations(delimitedString);
        populateDescriptions(delimitedString);
        allLines.add(Arrays.asList(delimitedString));
    }

    private void populateAnnotations(final String[] delimitedString) {
        for (final String annotation : getAnnotationsFromInputString(delimitedString)) {
            if (!annotations.contains(annotation)) {
                annotations.add(annotation);
            }
        }
    }

    private List<String> getAnnotationsFromInputString(final String[] delimitedString) {
        return Arrays.asList(delimitedString[ANNOTATION_COLUMN].split(ENTRY_FIELD_REGEX));
    }

    private void populateDescriptions(final String[] delimitedString) {
        descriptions.add(delimitedString[DESCRIPTION_COLUMN]);
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public List<List<String>> getAllLines() {
        return allLines;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

}
