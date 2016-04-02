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
    private Map<String, Entity> elements = new HashMap<>();

    private Entity convert(CSVEntry itemToConvert) {
        return new Entity(itemToConvert.getDescription());
    }

    private Entity convert(CSVEntry itemToConvert, Entity parent) {
        return new Entity(parent, itemToConvert.getDescription());
    }

    List<Entity> convert(List<CSVEntry> itemsToConvert) {
        List<Entity> entities = new ArrayList<>();

        for (CSVEntry csvEntry : itemsToConvert) {
            entities.addAll(processIndividualWords(csvEntry));
            entities.addAll(processCompoundWords(csvEntry));
        }

        return entities;
    }

    private List<Entity> processIndividualWords(final CSVEntry csvEntry) {
        List<Entity> lineEntities = new ArrayList<>();
        String[] descriptions = csvEntry.toString().split("\\s+");
        for (String description : descriptions) {
            if (!elements.containsKey(description)){
                final Entity entity = new Entity(description);
                lineEntities.add(entity);
                elements.put(description, entity);
            }
        }
        return lineEntities;
    }
//            Map<String, Entity> topLevelClasses = new HashMap<>();
//        for (CSVEntry csvEntry : itemsToConvert) {
//            if (wordAppearsInDescriptionAndAnnotation(csvEntry)) {
//                if (!topLevelClasses.containsKey(getFirstMatchingWord(csvEntry))) {
//                    System.out.println("wordAppearsInDescription -> " + csvEntry);
//                    Entity parent = new Entity(getFirstMatchingWord(csvEntry));
//                    topLevelClasses.put(getFirstMatchingWord(csvEntry), parent);
//                    entities.add(parent);
//                }
//            }
//
//            if (hasPreposition(csvEntry.getDescription())) {
//                Entity parent = getParentIfExists(topLevelClasses, csvEntry);
//                entities.add(convert(csvEntry, parent));
//                System.out.println("preposition -> " + csvEntry);
//            } else {
//                final Entity possibleParent = getparentForAnnotationIfExists(csvEntry, topLevelClasses);
//                if (possibleParent != null) {
//                    entities.add(convert(csvEntry, possibleParent));
//                } else {
//                    Entity parent = convert(csvEntry);
//                    topLevelClasses.put(csvEntry.getDescription(), parent);
//                    entities.add(convert(csvEntry));
//                }
//                System.out.println("non -> " + csvEntry);
//            }

    private Entity getparentForAnnotationIfExists(CSVEntry csvEntry, Map<String, Entity> topLevelClasses) {
        String[] splitString = csvEntry.toString().split(" ");
        for (String string : splitString) {
            if (topLevelClasses.containsKey(string)) {
                return topLevelClasses.get(string);
            }
        }
        return null;
    }

    private boolean wordAppearsInDescriptionAndAnnotation(CSVEntry csvEntry) {
        List<String> unique = new ArrayList<>();
        String[] splitString = csvEntry.toString().split(" ");
        for (String string : splitString) {
            if (unique.contains(string)) {
                return true;
            } else {
                unique.add(string);
            }
        }
        return false;
    }

    private String getFirstMatchingWord(CSVEntry csvEntry) {
        List<String> unique = new ArrayList<>();
        String[] splitString = csvEntry.toString().split(" ");
        for (String string : splitString) {
            if (unique.contains(string)) {
                return string;
            } else {
                unique.add(string);
            }
        }
        return null;
    }

    private boolean hasPreposition(String description) {
        matcher = pattern.matcher(description);
        return matcher.find();
    }


    private Entity getParentIfExists(Map<String, Entity> parentClasses, CSVEntry csvEntry) {
        final String parent = matcher.group(1);
        if (parentClasses.containsKey(parent)) {
            return parentClasses.get(parent);
        }

        final Entity possibleParent = getparentForAnnotationIfExists(csvEntry, parentClasses);
        if (possibleParent != null) {
            return possibleParent;
        }
        Entity newParent = new Entity(parent);
        parentClasses.put(parent, newParent);
        return newParent;
    }
}
