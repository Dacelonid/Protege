package ie.dacelonid.CSVParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ie.dacelonid.CSVParser.DelimeterConstants.*;

public class CsvParser {
    private final List<CSVEntry> allLines = new ArrayList<>();
    private final List<String> annotations = new ArrayList<>();
    private final List<String> descriptions = new ArrayList<>();
    private int annotationColumn;
    private int descriptionColumn;
    private int natureColumn;

    public void parseCsv(final List<String> inputData) {
        int lineNumber = 0;
        for (final String line : inputData) {
            if (lineNumber++ == 0) {
                processColumnNames(line);
            } else {
                processLine(line);
            }
        }
    }

    private void processColumnNames(final String line) {
        final String[] headings = line.split(COLUMN_FIELD_REGEX);
        for (int x = 0; x < headings.length; x++) {
            if (headings[x].equals(ANNOTATION_HEADING)) {
                annotationColumn = x;
            }
            if (headings[x].equals(DESCRIPTION_HEADING)) {
                descriptionColumn = x;
            }
            if (headings[x].equals(NATURE_HEADING)) {
                natureColumn = x;
            }
        }
    }

    private void processLine(final String line) {
        final String[] splitIntoColumns = line.split(COLUMN_FIELD_REGEX);
        String description = splitIntoColumns[descriptionColumn];
        List<String> annotations = getAnnotationsFromInputString(splitIntoColumns);
        String nature = splitIntoColumns[natureColumn];
        populateAnnotations(annotations);
        populateDescriptions(description);

        allLines.add(new CSVEntry(description, nature, annotations));
    }

    private List<String> getAnnotationsFromInputString(final String[] delimitedString) {
        return Arrays.asList(delimitedString[annotationColumn].split(ENTRY_FIELD_REGEX));
    }

    private void populateAnnotations(List<String> annotations) {
        annotations.stream().filter(annotation -> !this.annotations.contains(annotation)).forEach(this.annotations::add);
    }

    private void populateDescriptions(final String description) {
        descriptions.add(description);
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public List<CSVEntry> getAllLines() {
        return allLines;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }
}
