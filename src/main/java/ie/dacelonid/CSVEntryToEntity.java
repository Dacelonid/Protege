package ie.dacelonid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CSVEntryToEntity {

    private static final String PREPOSITION_REGEX = "(.*)\\s+(aboard|about|above|across|after|against|along|amid|among|anti|around|as|at|before|behind|below|beneath|beside|besides|between|beyond|but|by|concerning|considering|despite|down|during|except|excepting|excluding|following|for|from|in|inside|into|like|minus|near|of|off|on|onto|opposite|outside|over|past|per|plus|regarding|round|save|since|than|through|to|toward|towards|under|underneath|unlike|until|up|upon|versus|via|with|within|without)\\s+(.*)";
    private static final Pattern pattern = Pattern.compile(PREPOSITION_REGEX, Pattern.CASE_INSENSITIVE);
    private Matcher matcher;

    private Entity convert(CSVEntries itemToConvert) {
        return new Entity(itemToConvert.getDescription());
    }

    private Entity convert(CSVEntries itemToConvert, Entity parent) {
        return new Entity(parent, itemToConvert.getDescription());
    }

    List<Entity> convert(List<CSVEntries> itemsToConvert) {
        List<Entity> entities = new ArrayList<>();
        Map<String, Entity> topLevelClasses = new HashMap<>();
        for (CSVEntries csvEntry : itemsToConvert) {
            if (hasPreposition(csvEntry.getDescription())) {
                Entity parent = getParentIfExists(topLevelClasses, csvEntry);
                entities.add(convert(csvEntry, parent));
            } else {
                Entity parent = convert(csvEntry);
                topLevelClasses.put(csvEntry.getDescription(), parent);
                entities.add(convert(csvEntry));
            }
        }
        return entities;
    }

    private boolean hasPreposition(String description) {
        matcher = pattern.matcher(description);
        return matcher.find();
    }


    private Entity getParentIfExists(Map<String, Entity> parentClasses, CSVEntries csvEntry) {
        final String parent = matcher.group(1);
        if (parentClasses.containsKey(parent)) {
            return parentClasses.get(parent);
        }
        Entity newParent = new Entity(parent);
        parentClasses.put(parent, newParent);
        return newParent;
    }
}
