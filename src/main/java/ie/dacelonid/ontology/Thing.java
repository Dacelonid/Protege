package ie.dacelonid.ontology;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Thing implements RdfElement {
    private final Thing parent;
    private final String name;
    private final List<String> annotations;

    public Thing(final String name) {
        this.parent = null;
        this.name = name.replace(' ', '_');
        annotations = Collections.emptyList();
    }

    public Thing(final Thing parent, final String name) {
        this.parent = parent;
        this.name = name.replace(' ', '_');
        annotations = Collections.emptyList();
    }

    public Thing(final Thing parent, String name, List<String> annotations) {

        this.parent = parent;
        this.name = name.replace(' ', '_');
        this.annotations = annotations;
    }

    public Thing getParent() {
        return parent;
    }

    public String getName() {
        return "#" + name;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    @Override
    public String toString() {
        return "Thing:[ parent = (" + parent + "), Name = " + name + ", Annotations = " + Arrays.toString(annotations.toArray());
    }

    private String getParentString() {
        if (parent == null) {
            return "";
        }
        return " <SubClassOf>" + System.lineSeparator() + "<Class IRI=\"#" + name + "\"/>" + System.lineSeparator() +
                "<Class IRI=\"" + parent.getName() + "\"/> " + System.lineSeparator() + " </SubClassOf>" + System.lineSeparator();
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof Thing) {
            Thing other = (Thing) obj;
            return Objects.equals(this.name, other.name) && Objects.equals(this.parent, other.parent) && Objects.equals(annotations, other.annotations);
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return 13 + Objects.hashCode(name) + Objects.hashCode(parent) + Objects.hashCode(annotations);
    }

    protected abstract List<String> getAnnotationsFromHierarchy();
}
