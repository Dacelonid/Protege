package ie.dacelonid;

class Entity {
    private final String name;

    Entity(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "<Declaration>\n<Class IRI=\"#" + name + "\"/>\n</Declaration>\n";
    }
}
