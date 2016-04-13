package ie.dacelonid;

import java.util.Objects;

class Property implements RdfElement {

    private String name;
    private final boolean functional;
    private final String domain_entity;
    private final String range_entity;

    private Property(String name, boolean functional, String domain_entity, String range_entity) {
        this.name = name;
        this.functional = functional;
        this.domain_entity = domain_entity;
        this.range_entity = range_entity;
        this.name = "#" + name;
    }


    @Override
    public String getPrintableElement() {
        StringBuilder output = new StringBuilder();
        output.append("<Declaration>" ).append(System.lineSeparator());
        output.append("<ObjectProperty IRI=\"").append(getName()).append("\"/>").append(System.lineSeparator());
        output.append("</Declaration>" ).append(System.lineSeparator());
        if(functional){
            output.append("<FunctionalObjectProperty>").append(System.lineSeparator());
            output.append("<ObjectProperty IRI=\"").append(getName()).append("\"/>").append(System.lineSeparator());
            output.append("</FunctionalObjectProperty>").append(System.lineSeparator());
        }
        output.append("<ObjectPropertyDomain>").append(System.lineSeparator());
        output.append("<ObjectProperty IRI=\"").append(getName()).append("\"/>").append(System.lineSeparator());
        output.append("<Class IRI=\"").append(domain_entity).append("\"/>").append(System.lineSeparator());
        output.append("</ObjectPropertyDomain>").append(System.lineSeparator());
        output.append("<ObjectPropertyRange>").append(System.lineSeparator());
        output.append("<ObjectProperty IRI=\"").append(getName()).append("\"/>").append(System.lineSeparator());
        output.append("<Class IRI=\"").append(range_entity).append("\"/>").append(System.lineSeparator());
        output.append("</ObjectPropertyRange>").append(System.lineSeparator());
        return output.toString();
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + (functional ? 1 : 0);
        result = 31 * result + Objects.hashCode(domain_entity);
        result = 31 * result + Objects.hashCode(range_entity);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Property){
            Property other = (Property)obj;
            return Objects.equals(name, other.name) && Objects.equals(domain_entity, other.domain_entity) && Objects.equals(range_entity, other
                    .range_entity) && functional == other.functional;
        }
        return false;
    }

    static class PropertyBuilder {
        private String name;
        private boolean functional = false;
        private String domain_entity;
        private String range_entity;

        public PropertyBuilder name(String name) {
            this.name = name;
            return this;
        }

        Property build() {
            return new Property(name, functional, domain_entity, range_entity);
        }

        PropertyBuilder functional() {
            this.functional = true;
            return this;
        }

        PropertyBuilder domain(Entity domain_entity) {
            this.domain_entity = domain_entity.getName();
            return this;
        }

        PropertyBuilder range(Entity range_entity) {
            this.range_entity = range_entity.getName();
            return this;
        }
    }
}
