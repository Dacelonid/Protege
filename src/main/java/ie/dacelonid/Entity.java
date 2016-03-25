package ie.dacelonid;

class Entity {
    private final Entity parent;
    private final String name;

    Entity(final String name) {
        this.parent = null;
        this.name = name.replace(' ', '_');
    }

    Entity(final Entity parent, final String name) {
        this.parent = parent;
        this.name = name.replace(' ', '_');
    }

    @Override
    public String toString() {
        return "<Declaration>\n<Class IRI=\"#" + name + "\"/>" + System.lineSeparator() + "</Declaration>" +
                System.lineSeparator() + getParent();
    }

    private String getParent() {
        if (parent == null) {
            return "";
        }
        return " <SubClassOf>" + System.lineSeparator() + "<Class IRI=\"#" + name + "\"/>" + System.lineSeparator() +
                "<Class IRI=\"#" + parent.name + "\"/> " + System.lineSeparator() + " </SubClassOf>";
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof Entity) {
            Entity other = (Entity) obj;
            return fieldEquals(this.name, other.name) && fieldEquals(this.parent, other.parent);
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return 13 + (name == null ? 0 : name.hashCode()) + (parent == null ? 0 : parent.hashCode());
    }

    private boolean fieldEquals(Object field1, Object field2) {
        if (field1 == null) {
            return field2 == null;
        }
        return field1.equals(field2);
    }
}
