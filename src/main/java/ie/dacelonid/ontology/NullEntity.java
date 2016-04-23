package ie.dacelonid.ontology;

import java.util.Collections;
import java.util.List;

public class NullEntity extends Thing {

    public NullEntity() {
        super("");
    }

    @Override
    protected List<String> getAnnotationsFromHierarchy() {
        return Collections.emptyList();
    }

    @Override
    public String getPrintableElement() {
        return "";
    }
}
