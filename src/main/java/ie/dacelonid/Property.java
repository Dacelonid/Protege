package ie.dacelonid;

public class Property implements RdfElement {

    private String name;

    public Property(String name) {
        this.name = "#" + name;
    }

    @Override
    public String getPrintableElement() {
        return "<Declaration>" + System.lineSeparator() + "<ObjectProperty IRI=\"" + getName() + "\"/>" +
                System.lineSeparator() + "</Declaration>" + System.lineSeparator();
    }

    public String getName() {
        return name;
    }
}
