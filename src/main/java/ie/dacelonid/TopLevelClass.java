package ie.dacelonid;

import java.util.List;

public enum TopLevelClass {
    FLAG("flag for "), OTHER("Unknown");

    private String filterCondition;

    TopLevelClass(String filterCondition) {

        this.filterCondition = filterCondition;
    }

    public static TopLevelClass get(List<String> annotations) {
        if (annotations.contains("flag") && annotations.contains("other"))
            return FLAG;
        return OTHER;
    }

    public String getFilter() {
        return filterCondition;
    }
}
