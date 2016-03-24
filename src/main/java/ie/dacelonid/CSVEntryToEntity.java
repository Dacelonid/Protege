package ie.dacelonid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CSVEntryToEntity {

    Entity convert(CSVEntries itemToConvert) {
        return new Entity(itemToConvert.getDescription());
    }

    private Entity convert(CSVEntries itemToConvert, Entity parent) {
        return new Entity(parent, itemToConvert.getDescription());
    }

    List<Entity> convert(List<CSVEntries> itemsToConvert) {
        List<Entity> entities = new ArrayList<>();
        Map<String, Entity> parentClasses = new HashMap<>();
        for (CSVEntries csvEntry : itemsToConvert) {
            if (csvEntry.getAnnotations().size() == 1) {
                Entity parent = convert(csvEntry);
                parentClasses.put(csvEntry.getAnnotations().get(0), parent);
                entities.add(convert(csvEntry));
            } else {
                for (String annotation : csvEntry.getAnnotations()) {
                    if (parentClasses.containsKey(annotation)) {
                        entities.add(convert(csvEntry, parentClasses.get(annotation)));
                        break;
                    }
                }
            }
        }
        return entities;
    }
}
