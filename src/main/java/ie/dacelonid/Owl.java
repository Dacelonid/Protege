package ie.dacelonid;

import java.util.ArrayList;
import java.util.List;

class Owl {
    private static final String XML_VERSION_1_0 = "<?xml version=\"1.0\"?>";
    private static final String ONTOLOGY_NAMESPACE = "<Ontology xmlns=\"http://www.w3.org/2002/07/owl#\"";
    private static final String SEMANTIC_WEB = "xml:base=\"http://www.semanticweb.org/ken/ontologies/2016/0/untitled-ontology-6\"";
    private static final String RDF_SYNTAX = "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"";
    private static final String XML_NAMESPACE = "xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"";
    private static final String XML_SCHEMA = "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"";
    private static final String RDF_SCHEMA = "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"";
    private static final String ONTOLOGY = "ontologyIRI=\"http://www.semanticweb.org/ken/ontologies/2016/0/untitled-ontology-6\">";
    private static final String IRI = "<Prefix name=\"\" IRI=\"http://www.w3.org/2002/07/owl#\"/>";
    private static final String OWL_PREFIX = "<Prefix name=\"owl\" IRI=\"http://www.w3.org/2002/07/owl#\"/>";
    private static final String RDF_PREFIX = "<Prefix name=\"rdf\" IRI=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"/>";
    private static final String XML_PREFIX = "<Prefix name=\"xml\" IRI=\"http://www.w3.org/XML/1998/namespace\"/>";
    private static final String SCHEMA_PREFIX = "<Prefix name=\"xsd\" IRI=\"http://www.w3.org/2001/XMLSchema#\"/>";
    private static final String RDFS_PREFIX = "<Prefix name=\"rdfs\" IRI=\"http://www.w3.org/2000/01/rdf-schema#\"/>";
    private final List<Entity> entities = new ArrayList<>();
    private final List<RdfElement> disjointClasses = new ArrayList<>();
    private final List<RdfElement> rdfElements = new ArrayList<>();

    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(XML_VERSION_1_0).append(System.lineSeparator());
        output.append(ONTOLOGY_NAMESPACE).append(System.lineSeparator());
        output.append(SEMANTIC_WEB).append(System.lineSeparator());
        output.append(RDF_SYNTAX).append(System.lineSeparator());
        output.append(XML_NAMESPACE).append(System.lineSeparator());
        output.append(XML_SCHEMA).append(System.lineSeparator());
        output.append(RDF_SCHEMA).append(System.lineSeparator());
        output.append(ONTOLOGY).append(System.lineSeparator());
        output.append(IRI).append(System.lineSeparator());
        output.append(OWL_PREFIX).append(System.lineSeparator());
        output.append(RDF_PREFIX).append(System.lineSeparator());
        output.append(XML_PREFIX).append(System.lineSeparator());
        output.append(SCHEMA_PREFIX).append(System.lineSeparator());
        output.append(RDFS_PREFIX).append(System.lineSeparator());

        for (RdfElement rdfElement : rdfElements) {
            output.append(rdfElement.getPrintableElement());
        }


        output.append("</Ontology>");

        return output.toString();
    }

    void addEntity(RdfElement entity) {
        rdfElements.add(entity);
    }

    void addDisjointClasses(RdfElement disjointClass) {
        rdfElements.add(disjointClass);
    }

    void addProperty(RdfElement property) {
        rdfElements.add(property);
    }
}
