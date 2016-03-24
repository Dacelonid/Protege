package ie.dacelonid;

import java.util.Arrays;

class ListEntriesConverter extends ExpectedValuesConverter<CSVEntries> {
    private static final String COLUMN_DELIMITER = "\t";
    private static final String COLUMN_FIELD_REGEX = "\\s*" + COLUMN_DELIMITER + "\\s*";
    private static final String ENTRY_DELIMITER = ",";
    private static final String ENTRY_FIELD_REGEX = "\\s*" + ENTRY_DELIMITER + "\\s*";

    @Override
    public CSVEntries getExpectedValues(final String value) {
        String[] split = value.split(COLUMN_FIELD_REGEX);
        return new CSVEntries(split[0], Arrays.asList(split[2].split(ENTRY_FIELD_REGEX)));
    }
}