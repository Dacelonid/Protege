package ie.dacelonid;

class Entity {
    private final Entity parent;
    private final String name;

    Entity(final String name) {
        this.parent = null;
        this.name = name;
    }

    Entity(final Entity parent, final String name) {
        this.parent = parent;
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder entity = new StringBuilder();
        entity.append("<Declaration>\n<Class IRI=\"#");
        entity.append(name);
        entity.append("\"/>");
        entity.append(System.lineSeparator());
        entity.append("</Declaration>");
        entity.append(System.lineSeparator());
        entity.append(getParent());
        return entity.toString();
    }

    private String getParent() {
        if (parent == null) {
            return "";
        }
        return " <SubClassOf>\n <Class IRI=\"#" + name + "\"/>\n <Class IRI=\"#" + parent.name + "\"/>\n  </SubClassOf>";
    }
}
