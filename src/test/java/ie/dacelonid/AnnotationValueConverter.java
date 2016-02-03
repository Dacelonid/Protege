package ie.dacelonid;

public class AnnotationValueConverter extends ExpectedValuesConverter<String> {

    @Override
    public String getExpectedValues(final String value) {
        return value;
    }
}
