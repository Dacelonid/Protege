package ie.dacelonid.ontology;

import java.util.List;

public class Annotation extends Thing {
    public Annotation(String name) {
        super(name);
    }

    public Annotation(Thing parent, String name) {
        super(parent, name);
    }

    public Annotation(Thing parent, String name, List<String> annotations) {
        super(parent, name, annotations);
    }
}
