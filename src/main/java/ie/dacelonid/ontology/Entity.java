package ie.dacelonid.ontology;

import java.util.List;

public class Entity extends Thing {

    public Entity(String name) {
        super(name);
    }

    public Entity(Thing parent, String name) {
        super(parent, name);
    }

    public Entity(Thing parent, String name, List<String> annotations) {
        super(parent, name, annotations);
    }
}
