package ie.dacelonid;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DisjointClasses implements RdfElement {

    private final List<String> disjointClasses = new ArrayList<>();

    public DisjointClasses(String... disjointClasses) {
        for (String disjointClass : disjointClasses) {
            this.disjointClasses.add(disjointClass);
        }
    }

    @Override
    public String getPrintableElement() {
        StringBuilder output = new StringBuilder();
        output.append("<DisjointClasses>").append(System.lineSeparator());
        for (String disjointClass : disjointClasses) {
            output.append("<Class IRI=\"" + disjointClass + "\"/>").append(System.lineSeparator());
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
        } return false;
    }
}
