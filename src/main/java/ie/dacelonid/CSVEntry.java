package ie.dacelonid;

import java.util.List;
import java.util.Objects;

class CSVEntry {
    private final String description;
    private final String nature;
    private final List<String> annotations;

    CSVEntry(String description, String nature, List<String> annotations) {
        this.description = description;
        this.nature = nature;
        this.annotations = annotations;
    }

    @Override
    public final int hashCode() {
        return (description == null ? 0 : description.hashCode()) + (annotations == null ? 0 : annotations.hashCode()) + (nature == null ? 0 :
                nature.hashCode());
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof CSVEntry) {
            CSVEntry other = (CSVEntry) obj;
            return Objects.equals(this.annotations, other.annotations) && Objects.equals(this.description, other.description) && Objects.equals(
                    nature, other.nature);
        }
        return false;
    }

    String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(description);
        builder.append("\t");
        builder.append(nature);
        builder.append("\t");
        for(String annotation:annotations){
            builder.append(annotation.trim()).append(" ");
        }
        return builder.toString();
    }

    String getNature() {
        return nature;
    }

    public List<String> getAnnotations() {
        return annotations;
    }
}
