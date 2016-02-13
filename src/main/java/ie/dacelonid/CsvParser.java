package ie.dacelonid;

import java.util.*;
import java.util.stream.Collectors;

public class CsvParser {
    private static final String COLUMN_DELIMITER = "\t";
    private static final String COLUMN_FIELD_REGEX = "\\s*" + COLUMN_DELIMITER + "\\s*";
    private static final String ENTRY_DELIMITER = ",";
    private static final String ENTRY_FIELD_REGEX = "\\s*" + ENTRY_DELIMITER + "\\s*";
    private static final String ANNOTATION_HEADING = "Annotations";
    private static final String NATURE_HEADING = "Nature";
    private static final String DESCRIPTION_HEADING = "Description";
    private final List<List<String>> allLines = new ArrayList<>();
    private final List<String> annotations = new ArrayList<>(); // @TODO should be a Set but then I need to sort it in the tests
    private final List<String> descriptions = new ArrayList<>();
    private int annotationColumn;
    private int descriptionColumn;
    private int natureColumn;
    private Map<String, List<String>> emojiMap = new HashMap<>();

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
        final String[] splitLine = line.split(COLUMN_FIELD_REGEX);
        String description = splitLine[descriptionColumn];
        List<String> annotations = getAnnotationsFromInputString(splitLine);
        emojiMap.put(description, annotations);

        populateAnnotations(annotations);
        populateDescriptions(description);

        allLines.add(Arrays.asList(splitLine));
    }

    private void populateAnnotations(List<String> annotations) {
        for (final String annotation : annotations) {
            if (!this.annotations.contains(annotation)) {
                this.annotations.add(annotation);
            }
        }
    }

    private List<String> getAnnotationsFromInputString(final String[] delimitedString) {
        return Arrays.asList(delimitedString[annotationColumn].split(ENTRY_FIELD_REGEX));
    }

    private void populateDescriptions(final String description) {
        descriptions.add(description);
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

    public List<String> getEmojiContainingAnnotation(final String annotation) {
        return emojiMap.entrySet().stream().filter(p -> p.getValue().contains((annotation))).map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
