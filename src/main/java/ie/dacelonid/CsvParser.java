package ie.dacelonid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvParser {
    private static final int ANNOTATION_COLUMN = 2;

    private static final String COLUMN_DELIMITER = "\t";
    private static final String COLUMN_FIELD_REGEX = "\\s*" + COLUMN_DELIMITER + "\\s*";

    private static final String ENTRY_DELIMITER = ",";
    private static final String ENTRY_FIELD_REGEX = "\\s*" + ENTRY_DELIMITER + "\\s*";

    private final List<List<String>> allLines = new ArrayList<>();
    private final List<String> annotations = new ArrayList<>();

    public void parseCsv(final List<String> inputData) {
        String[] delimitedString;
        for (final String line : inputData) {
            delimitedString = line.split(COLUMN_FIELD_REGEX);
            allLines.add(Arrays.asList(delimitedString));
            annotations.addAll(getAnnotationsFromInputString(delimitedString[ANNOTATION_COLUMN]));
        }
    }

    private List<String> getAnnotationsFromInputString(final String annotations) {
        return Arrays.asList(annotations.split(ENTRY_FIELD_REGEX));
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public List<List<String>> getAllLines() {
        return allLines;
    }

}
