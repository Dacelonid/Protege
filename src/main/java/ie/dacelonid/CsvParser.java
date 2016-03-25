package ie.dacelonid;

import java.util.*;

class CsvParser {
    private static final String COLUMN_DELIMITER = "\t";
    private static final String COLUMN_FIELD_REGEX = "\\s*" + COLUMN_DELIMITER + "\\s*";
    private static final String ENTRY_DELIMITER = ",";
    private static final String ENTRY_FIELD_REGEX = "\\s*" + ENTRY_DELIMITER + "\\s*";
    private static final String ANNOTATION_HEADING = "Annotations";
    private static final String NATURE_HEADING = "Nature";
    private static final String DESCRIPTION_HEADING = "Description";
    private final List<CSVEntries> allLines = new ArrayList<>();
    private final List<String> annotations = new ArrayList<>(); // @TODO should be a Set but then I need to sort it in the tests
    private final List<String> descriptions = new ArrayList<>();
    private final Map<String, List<String>> emojiMap = new HashMap<>();
    private final Map<TopLevelClass, List<String>> classMap = new HashMap<>();
    private int annotationColumn;
    private int descriptionColumn;
    private int natureColumn;

    void parseCsv(final List<String> inputData) {
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
        final String[] splitLine = line.split(COLUMN_FIELD_REGEX);
        String description = splitLine[descriptionColumn];
        List<String> annotations = getAnnotationsFromInputString(splitLine);
        TopLevelClass topLevelClass = TopLevelClass.get(annotations);
        emojiMap.put(description, annotations);
        List<String> descriptions;
        if (classMap.containsKey(topLevelClass)) {
            descriptions = classMap.get(topLevelClass);
            descriptions.add(description);
        } else {
            descriptions = new ArrayList<>();
            descriptions.add(description);
        }
        classMap.put(topLevelClass, descriptions);

        populateAnnotations(annotations);
        populateDescriptions(description);

        allLines.add(new CSVEntries(description, annotations));
    }

    private void populateAnnotations(List<String> annotations) {
        annotations.stream().filter(annotation -> !this.annotations.contains(annotation)).forEach(this.annotations::add);
    }

    private List<String> getAnnotationsFromInputString(final String[] delimitedString) {
        return Arrays.asList(delimitedString[annotationColumn].split(ENTRY_FIELD_REGEX));
    }

    private void populateDescriptions(final String description) {
        descriptions.add(description);
    }

    List<String> getAnnotations() {
        return annotations;
    }

    List<CSVEntries> getAllLines() {
        return allLines;
    }

    List<String> getDescriptions() {
        return descriptions;
    }
}
