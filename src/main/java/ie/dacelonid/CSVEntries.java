package ie.dacelonid;

import java.util.List;

class CSVEntries {
    private final String description;
    private final List<String> annotations;

    CSVEntries(String description, List<String> annotations) {
        this.description = description;
        this.annotations = annotations;
    }

    @Override
    public final int hashCode() {
        return (description == null ? 0 : description.hashCode()) + (annotations == null ? 0 : annotations.hashCode());
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof CSVEntries) {
            CSVEntries other = (CSVEntries) obj;
            return fieldEquals(this.annotations, other.annotations) && fieldEquals(this.description, other.description);
        }
        return false;
    }

    private boolean fieldEquals(Object field1, Object field2) {
        if (field1 == null) {
            return field2 == null;
        }
        return field1.equals(field2);
    }
}
