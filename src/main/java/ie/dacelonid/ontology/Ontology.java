package ie.dacelonid.ontology;

import java.util.ArrayList;
import java.util.List;

public class Ontology {
    private final List<Thing> entities = new ArrayList<>();
    private final List<RdfElement> disjointClasses = new ArrayList<>();
    private final List<RdfElement> rdfElements = new ArrayList<>();

    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(OntologyConstants.XML_VERSION_1_0).append(System.lineSeparator());
        output.append(OntologyConstants.ONTOLOGY_NAMESPACE).append(System.lineSeparator());
        output.append(OntologyConstants.SEMANTIC_WEB).append(System.lineSeparator());
        output.append(OntologyConstants.RDF_SYNTAX).append(System.lineSeparator());
        output.append(OntologyConstants.XML_NAMESPACE).append(System.lineSeparator());
        output.append(OntologyConstants.XML_SCHEMA).append(System.lineSeparator());
        output.append(OntologyConstants.RDF_SCHEMA).append(System.lineSeparator());
        output.append(OntologyConstants.ONTOLOGY).append(System.lineSeparator());
        output.append(OntologyConstants.IRI).append(System.lineSeparator());
        output.append(OntologyConstants.OWL_PREFIX).append(System.lineSeparator());
        output.append(OntologyConstants.RDF_PREFIX).append(System.lineSeparator());
        output.append(OntologyConstants.XML_PREFIX).append(System.lineSeparator());
        output.append(OntologyConstants.SCHEMA_PREFIX).append(System.lineSeparator());
        output.append(OntologyConstants.RDFS_PREFIX).append(System.lineSeparator());

        for (RdfElement rdfElement : rdfElements) {
            output.append(rdfElement.getPrintableElement());
        }


        output.append("</Ontology>");

        return output.toString();
    }

    public void addEntity(RdfElement entity) {
        rdfElements.add(entity);
    }

    void addDisjointClasses(RdfElement disjointClass) {
        rdfElements.add(disjointClass);
    }

    public void addProperty(RdfElement property) {
        rdfElements.add(property);
    }
}
