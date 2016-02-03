package ie.dacelonid;

import java.util.Arrays;
import java.util.List;

public class ListValueConverter extends ExpectedValuesConverter<List<String>> {

    @Override
    public List<String> getExpectedValues(final String value) {
        return Arrays.asList(value.split("\\s*\t\\s*"));
    }
}
