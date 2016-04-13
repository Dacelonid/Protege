package ie.dacelonid;

import java.util.Objects;

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
        return "<Declaration>\n<Class IRI=\"" + getName() + "\"/>" + System.lineSeparator() + "</Declaration>" +
                System.lineSeparator() + getParent();
    }

    protected String getParent() {
        if (parent == null) {
            return "";
        }
        return " <SubClassOf>" + System.lineSeparator() + "<Class IRI=\"#" + name + "\"/>" + System.lineSeparator() +
                "<Class IRI=\"" + parent.getName()  + "\"/> " + System.lineSeparator() + " </SubClassOf>" + System.lineSeparator();
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof Entity) {
            Entity other = (Entity) obj;
            return Objects.equals(this.name, other.name) && Objects.equals(this.parent, other.parent);
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return 13 + (name == null ? 0 : name.hashCode()) + (parent == null ? 0 : parent.hashCode());
    }

    public String getName() {
        return "#" + name;
    }
}
