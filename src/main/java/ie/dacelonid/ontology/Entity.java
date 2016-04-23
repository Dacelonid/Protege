package ie.dacelonid.ontology;

import java.util.ArrayList;
import java.util.List;

public class Entity extends Thing {
    private static final NullEntity NULL_ENTITY = new NullEntity();

    public Entity(String name) {
        super(NULL_ENTITY, name);
    }

    public Entity(Thing parent, String name, List<String> annotations) {
        super(parent, name, annotations);
    }

    @Override
    public String getPrintableElement() {
        return "<Declaration>\n<Class IRI=\"" + getName() + "\"/>" + System.lineSeparator() + "</Declaration>" +
                System.lineSeparator() + getParentString() + System.lineSeparator() + getAnnotationString();
    }

    private String getParentString() {
        if (getParent() == NULL_ENTITY) {
            return "";
        }
        return " <SubClassOf>" + System.lineSeparator() + "<Class IRI=\"" + getName() + "\"/>" + System.lineSeparator() +
                "<Class IRI=\"" + getParent().getName() + "\"/> " + System.lineSeparator() + " </SubClassOf>" + System.lineSeparator();
    }

    private String getAnnotationString() {
        List<String> annotationsNotInherited = getAnnotationsNotInherited();
        if (annotationsNotInherited.size() > 0) {
            return getAnnotationsString(annotationsNotInherited);
        }
        return "";
    }

    private String getAnnotationsString(List<String> annotationsNotInherited) {
        StringBuilder output = new StringBuilder();
        output.append("<SubClassOf>").append(System.lineSeparator());
        output.append("<Class IRI=\"").append(getName()).append("\"/>").append(System.lineSeparator());
        output.append("<ObjectAllValuesFrom>").append(System.lineSeparator());
        output.append("<ObjectProperty IRI=\"#has_annotation\"/>").append(System.lineSeparator());
        if (hasSingleAnnotation(annotationsNotInherited)) {
            output.append("<Class IRI=\"").append(getAnnotationString(annotationsNotInherited.get(0))).append("\"/>").append(System.lineSeparator());
        } else {
            getMultipleAnnotations(annotationsNotInherited, output);
        }
        output.append("</ObjectAllValuesFrom>").append(System.lineSeparator());
        output.append("</SubClassOf>").append(System.lineSeparator());
        return output.toString();
    }

    private boolean hasSingleAnnotation(List<String> annotations) {
        return annotations.size() == 1;
    }

    private void getMultipleAnnotations(List<String> annotationsNotInherited, StringBuilder output) {
        output.append("<ObjectIntersectionOf>").append(System.lineSeparator());
        for (String annotation : annotationsNotInherited) {
            output.append("<Class IRI=\"").append(getAnnotationString(annotation)).append("\"/>").append(System.lineSeparator());
        }
        output.append("</ObjectIntersectionOf>").append(System.lineSeparator());
    }

    private String getAnnotationString(final String name) {
        return "#" + name.replaceAll(" ", "_") + "_annotation";
    }

    private List<String> getAnnotationsNotInherited() {

        List<String> annotationsNotInherited = new ArrayList<>(getAnnotations());
        annotationsNotInherited.removeAll(getAnnotationsFromHierarchy());
        return annotationsNotInherited;
    }

    protected List<String> getAnnotationsFromHierarchy() {
        List<String> tempList = new ArrayList<>(getParent().getAnnotations());
        tempList.addAll(getParent().getAnnotationsFromHierarchy());
        return tempList;
    }

}
