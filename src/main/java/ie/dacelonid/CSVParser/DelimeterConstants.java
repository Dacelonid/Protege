package ie.dacelonid.CSVParser;

public class DelimeterConstants {
    public static final String ANNOTATION_HEADING = "Annotations";
    public static final String NATURE_HEADING = "Nature";
    public static final String DESCRIPTION_HEADING = "Description";
    private static final String COLUMN_DELIMITER = "\t";
    public static final String COLUMN_FIELD_REGEX = "\\s*" + COLUMN_DELIMITER + "\\s*";
    private static final String ENTRY_DELIMITER = ",";
    public static final String ENTRY_FIELD_REGEX = "\\s*" + ENTRY_DELIMITER + "\\s*";
}
