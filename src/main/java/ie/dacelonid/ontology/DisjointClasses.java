package ie.dacelonid.ontology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DisjointClasses implements RdfElement {

    private final List<String> disjointClasses = new ArrayList<>();

    public DisjointClasses(String... disjointClasses) {
        Collections.addAll(this.disjointClasses, disjointClasses);
    }

    @Override
    public String getPrintableElement() {
        StringBuilder output = new StringBuilder();
        output.append("<DisjointClasses>").append(System.lineSeparator());
        for (String disjointClass : disjointClasses) {
            output.append("<Class IRI=\"").append(disjointClass).append("\"/>").append(System.lineSeparator());
        }
        output.append("</DisjointClasses>").append(System.lineSeparator());
        return output.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(disjointClasses);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DisjointClasses) {
            DisjointClasses other = (DisjointClasses) obj;
            return Objects.equals(disjointClasses, other.disjointClasses);
        }
        return false;
    }
}
