package ie.dacelonid.ontology;

import java.util.Collections;
import java.util.List;

public class Annotation extends Thing {

    private static final NullEntity NULL_ENTITY = new NullEntity();

    public Annotation(String name) {
        super(NULL_ENTITY, name);
    }

    public Annotation(Thing parent, String name) {
        super(parent, name + "_annotation");
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
        return "";
    }


    protected List<String> getAnnotationsFromHierarchy() {
        return Collections.emptyList();
    }
}
