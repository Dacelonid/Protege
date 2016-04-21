package ie.dacelonid;

import java.util.*;

class Entity implements RdfElement {
    private final Entity parent;
    private final String name;
    private final List<String> annotations;

    Entity(final String name) {
        this.parent = null;
        this.name = name.replace(' ', '_');
        annotations = Collections.emptyList();
    }

    Entity(final Entity parent, final String name) {
        this.parent = parent;
        this.name = name.replace(' ', '_');
        annotations = Collections.emptyList();
    }

    public Entity(final Entity parent, String name, List<String> annotations) {

        this.parent = parent;
        this.name = name.replace(' ', '_');
        this.annotations = annotations;
    }

    @Override
    public String toString() {
        return "Entity:[ parent = (" + parent + "), Name = " + name + ", Annotations = " + Arrays.toString(annotations.toArray());
    }

    private String getParent() {
        if (parent == null) {
            return "";
        }
        return " <SubClassOf>" + System.lineSeparator() + "<Class IRI=\"#" + name + "\"/>" + System.lineSeparator() +
                "<Class IRI=\"" + parent.getName() + "\"/> " + System.lineSeparator() + " </SubClassOf>" + System.lineSeparator();
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof Entity) {
            Entity other = (Entity) obj;
            return Objects.equals(this.name, other.name) && Objects.equals(this.parent, other.parent) && Objects.equals(annotations,
                                                                                                                        other.annotations);
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return 13 + Objects.hashCode(name) + Objects.hashCode(parent) + Objects.hashCode(annotations);
    }

    public String getName() {
        return "#" + name;
    }

    @Override
    public String getPrintableElement() {
        return "<Declaration>\n<Class IRI=\"" + getName() + "\"/>" + System.lineSeparator() + "</Declaration>" +
                System.lineSeparator() + getParent() + System.lineSeparator() + getAnnotationString();
    }

    private String getAnnotationString() {
        List<String> annotationsNotInherited = getAnnotationsNotInherited();
        StringBuilder output = new StringBuilder();
        if (annotationsNotInherited.size() > 0) {
            output.append("<SubClassOf>").append(System.lineSeparator());
            output.append("<Class IRI=\"").append(getName()).append("\"/>").append(System.lineSeparator());
            output.append("<ObjectAllValuesFrom>").append(System.lineSeparator());
            output.append("<ObjectProperty IRI=\"#has_annotation\"/>").append(System.lineSeparator());
            if (annotationsNotInherited.size() > 1) {
                output.append("<ObjectIntersectionOf>").append(System.lineSeparator());
                for (String annotation : annotationsNotInherited) {
                    output.append("<Class IRI=\"").append(getAnnotationString(annotation)).append("\"/>").append(System.lineSeparator());
                }
            } else {
                output.append("<Class IRI=\"").append(getAnnotationString(annotationsNotInherited.get(0))).append("\"/>").append(
                        System.lineSeparator());
            }
            if (annotationsNotInherited.size() > 1) {
                output.append("</ObjectIntersectionOf>").append(System.lineSeparator());
            }
            output.append("</ObjectAllValuesFrom>").append(System.lineSeparator());
            output.append("</SubClassOf>").append(System.lineSeparator());
        }
        return output.toString();
    }

    private String getAnnotationString(final String name) {
        return "#" + name.replaceAll(" ", "_") + "_annotation";
    }

    private List<String> getAnnotationsNotInherited() {
        List<String> annotationsNotInherited = new ArrayList<>();
        annotationsNotInherited.addAll(annotations);
        if (parent != null) {
            annotationsNotInherited.removeAll(getAnnotationsFromHierarchy());
        }
        return annotationsNotInherited;
    }

    private List<String> getAnnotationsFromHierarchy() {
        List<String> tempList = new ArrayList<>();
        if (parent != null) {
            tempList.addAll(parent.annotations);
            tempList.addAll(parent.getAnnotationsFromHierarchy());
        }
        return tempList;
    }
}
