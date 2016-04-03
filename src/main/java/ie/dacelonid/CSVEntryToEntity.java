package ie.dacelonid;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CSVEntryToEntity {

    private static final String PREPOSITION_REGEX_1 = "(aboard|about|above|across|after|against|along|amid|among|anti|around|as|at|before|behind" +
            "|below|beneath|beside|besides|between|beyond|but|by|concerning|considering|despite|down|during|except|excepting|excluding|following" +
            "|for|from|in|inside|into|like|minus|near|of|off|on|onto|opposite|outside|over|past|per|plus|regarding|round|save|since|than|through|to" +
            "|toward|towards|under|underneath|unlike|until|up|upon|versus|via|with|with a|within|without)";
    private static final String PREPOSITION_REGEX = "(.*)\\s+" + PREPOSITION_REGEX_1 + "\\s+(.*)";
    private static final Pattern pattern = Pattern.compile(PREPOSITION_REGEX, Pattern.CASE_INSENSITIVE);
    private static final Pattern pattern1 = Pattern.compile(PREPOSITION_REGEX_1, Pattern.CASE_INSENSITIVE);
    private final Map<String, Entity> elements = new HashMap<>();
    private Matcher matcher;

    List<Entity> convert(List<CSVEntry> itemsToConvert) {
        List<Entity> entities = new ArrayList<>();

        for (CSVEntry csvEntry : itemsToConvert) {
            if (descriptionNeedsSpecialHandling(csvEntry)) {
                entities.addAll(processSpecialCases(csvEntry));
            } else {
                entities.addAll(processCompoundWords(csvEntry));
                entities.addAll(processDescription(csvEntry));
            }
        }

        return entities;
    }

    private List<Entity> processSpecialCases(CSVEntry csvEntry) {
        Entity parent;
        if ("text".equals(csvEntry.getNature())) {
            parent = new Entity("text");
            if (!elements.containsKey("text")) elements.put("text", parent);
        } else {
            parent = new Entity("ideograph");
            if (!elements.containsKey("ideograph")) elements.put("ideograph", parent);
        }
        Entity subClasses = new Entity(parent, csvEntry.getDescription());
        elements.put(csvEntry.getDescription(), subClasses);
        return Arrays.asList(parent, subClasses);
    }

    private boolean descriptionNeedsSpecialHandling(CSVEntry nature) {
        return "text".equals(nature.getNature()) || nature.getDescription().contains("ideograph");
    }

    private List<Entity> processDescription(CSVEntry csvEntry) {
        if (elements.containsKey(csvEntry.getDescription())) {
            return Collections.emptyList();
        }
        List<Entity> entities = new ArrayList<>();
        Entity parent;
        if (hasPreposition(csvEntry.getDescription())) {
            parent = elements.get(getLeftHandSideOfPreposition());
        } else {
            String lastWord = getLastWord(csvEntry.getDescription());
            parent = elements.get(lastWord);
        }
        Entity entity = new Entity(parent, csvEntry.getDescription());
        entities.add(entity);
        elements.put(csvEntry.getDescription(), entity);
        return entities;

    }

    private List<Entity> processIndividualWords(String descriptions) {
        List<Entity> lineEntities = new ArrayList<>();
        String lastWord = getLastWord(descriptions);
        if (!isPreposition(lastWord)) {
            if (!elements.containsKey(lastWord)) {
                final Entity entity = new Entity(lastWord);
                    lineEntities.add(entity);
                elements.put(lastWord, entity);
            }
        }
        return lineEntities;
    }

    private String getLastWord(String descriptions) {
        return descriptions.replaceAll("^.*?(\\w+)\\W*$", "$1");
    }

    private List<Entity> processCompoundWords(CSVEntry csvEntry) {
        String description = csvEntry.getDescription();
        if (hasPreposition(description)) {
            return handlePrepositions();
        } else {
            return processIndividualWords(description);
        }
    }

    private List<Entity> handlePrepositions() {
        List<Entity> entities = new ArrayList<>();
        String key1 = getLeftHandSideOfPreposition();
        entities.addAll(processIndividualWords(key1));
        entities.addAll(handleElementOfPreposition(key1));
        return entities;
    }

    private List<Entity> handleElementOfPreposition(String key) {
        List<Entity> entities = new ArrayList<>();
        if (elements.containsKey(key)) {
            return Collections.emptyList();
        }
        Entity possibleParent = getPossibleParent(key);
        Entity entity = new Entity(possibleParent, key);
        entities.add(entity);
        elements.put(key, entity);
        return entities;
    }

    private Entity getPossibleParent(String key) {
        for (String keyPart : key.split(" ")) {
            if (elements.containsKey(keyPart)) {
                return elements.get(keyPart);
            }
        }
        return null;
    }

    private String getLeftHandSideOfPreposition() {
        return matcher.group(1);
    }


    private boolean hasPreposition(String description) {
        matcher = pattern.matcher(description);
        return matcher.find();
    }

    private boolean isPreposition(String description) {
        return pattern1.matcher(description).matches();
    }
}
