package ie.dacelonid;

public class StringValueConverter extends ExpectedValuesConverter<String> {

    @Override
    public String getExpectedValues(final String value) {
        return value;
    }
}
