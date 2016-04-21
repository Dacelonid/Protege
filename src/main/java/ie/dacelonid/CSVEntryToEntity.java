package ie.dacelonid;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CSVEntryToEntity {

    private static final String PREPOSITION_REGEX = "(aboard|about|above|across|after|against|along|amid|among|anti|around|as|at|before|behind" +
            "|below|beneath|beside|besides|between|beyond|but|by|concerning|considering|despite|down|during|except|excepting|excluding|following" +
            "|for|from|in|inside|into|like|minus|near|of|off|on|onto|opposite|outside|over|past|per|plus|regarding|round|save|since|than|through|to" +
            "|toward|towards|under|underneath|unlike|until|up|upon|versus|via|with|with a|within|without)";
    private static final String PREPOSITION_REGEX_WITH_CAPTURE = "(.*?)\\s+" + PREPOSITION_REGEX + "\\s+(.*)";
    private static final Pattern patternWithCapture = Pattern.compile(PREPOSITION_REGEX_WITH_CAPTURE, Pattern.CASE_INSENSITIVE);
    private static final Pattern pattern = Pattern.compile(PREPOSITION_REGEX, Pattern.CASE_INSENSITIVE);
    private final Map<String, Entity> elements = new HashMap<>();
    private final Entity annotation_Entity = new Entity("annotations");
    private final Entity classes_Entity = new Entity("classes");
    private Matcher matcher;
    private List<RdfElement> rdfElements;

    public void convert(List<CSVEntry> itemsToConvert) {
        rdfElements = new ArrayList<>();

        rdfElements.add(annotation_Entity);
        rdfElements.add(classes_Entity);
        rdfElements.add(new DisjointClasses(annotation_Entity.getName(), classes_Entity.getName()));
        Property property = new Property.PropertyBuilder().name("has_annotation").functional().domain(classes_Entity).range(
                annotation_Entity).build();
        rdfElements.add(property);

        for (CSVEntry csvEntry : itemsToConvert) {
            rdfElements.addAll(processAnnotations(csvEntry));
            if (descriptionNeedsSpecialHandling(csvEntry)) {
                rdfElements.addAll(processSpecialCases(csvEntry));
            } else {
                rdfElements.addAll(processCompoundWords(csvEntry));
                rdfElements.addAll(processDescription(csvEntry));
            }
        }
    }

    List<RdfElement> getProperties() {
        return rdfElements;
    }

    private List<Entity> processAnnotations(CSVEntry csvEntry) {
        List<Entity> newEntities = new ArrayList<>();
        for (String annotation : csvEntry.getAnnotations()) {
            if (elements.containsKey(annotation + "_annotation")) {
                continue;
            }
            Entity newEntity = new Entity(annotation_Entity, annotation + "_annotation");
            newEntities.add(newEntity);
            elements.put(annotation + "_annotation", newEntity);
        }
        return newEntities;
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
        Entity subClasses = new Entity(parent, csvEntry.getDescription(), csvEntry.getAnnotations());
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
            if (hasMultipleWordParent(getLeftHandSideOfPreposition())) {
                System.out.println(getMultipleWordParent(getLeftHandSideOfPreposition()));
                parent = getMultipleWordParent(getLeftHandSideOfPreposition());
            } else {
                parent = elements.get(getLeftHandSideOfPreposition());
            }
        } else {
            if (hasMultipleWordParent(csvEntry.getDescription())) {
                parent = getMultipleWordParent(csvEntry.getDescription());
            } else {
                String lastWord = getLastWord(csvEntry.getDescription());
                parent = elements.get(lastWord);
            }
        }
        Entity Entity = new Entity(parent, csvEntry.getDescription(), csvEntry.getAnnotations());
        entities.add(Entity);
        elements.put(csvEntry.getDescription(), Entity);
        return entities;

    }

    private Entity getMultipleWordParent(String leftHandSideOfPreposition) {
        if (elements.containsKey(leftHandSideOfPreposition)) {
            return elements.get(leftHandSideOfPreposition);
        }
        String[] splitWords = leftHandSideOfPreposition.split(" ", 2);
        while (splitWords.length == 2) {
            if (elements.containsKey(splitWords[1])) {
                return elements.get(splitWords[1]);
            } else {
                splitWords = splitWords[1].split(" ", 2);
            }
        }
        return elements.get(splitWords[0]);
    }

    private boolean hasMultipleWordParent(String leftHandSideOfPreposition) {
        if (elements.containsKey(leftHandSideOfPreposition)) {
            return true;
        }
        String[] splitWords = leftHandSideOfPreposition.split(" ", 2);
        while (splitWords.length == 2) {
            if (elements.containsKey(splitWords[1])) {
                return true;
            } else {
                splitWords = splitWords[1].split(" ", 2);
            }
        }

        return false;
    }

    private List<Entity> processIndividualWords(String descriptions, CSVEntry csvEntry) {
        List<Entity> lineEntities = new ArrayList<>();
        String lastWord = getLastWord(descriptions);
        if (!isPreposition(lastWord)) {
            if (!elements.containsKey(lastWord)) {
                final Entity Entity = new Entity(classes_Entity, lastWord, getMatchingAnnotations(lastWord, csvEntry));
                lineEntities.add(Entity);
                elements.put(lastWord, Entity);
            }
        }
        return lineEntities;
    }

    private List<String> getMatchingAnnotations(String name, CSVEntry csvEntry) {
        for (String annotation : csvEntry.getAnnotations()) {
            if (annotation.equals(name)) {
                return Collections.singletonList(name);
            }
        }
        return csvEntry.getAnnotations();
    }

    private String getLastWord(String descriptions) {
        return descriptions.replaceAll("^.*?(\\w+)\\W*$", "$1");
    }

    private List<Entity> processCompoundWords(CSVEntry csvEntry) {
        String description = csvEntry.getDescription();
        if (hasPreposition(description)) {
            return handlePrepositions(csvEntry);
        } else {
            return processIndividualWords(description, csvEntry);
        }
    }

    private List<Entity> handlePrepositions(CSVEntry csvEntry) {
        List<Entity> entities = new ArrayList<>();
        String key1 = getLeftHandSideOfPreposition();
        entities.addAll(processIndividualWords(key1, csvEntry));
        entities.addAll(handleElementOfPreposition(key1, csvEntry));
        return entities;
    }

    private List<Entity> handleElementOfPreposition(String key, CSVEntry csvEntry) {
        List<Entity> entities = new ArrayList<>();
        if (elements.containsKey(key)) {
            return Collections.emptyList();
        }
        Entity possibleParent = getPossibleParent(key);
        Entity Entity = new Entity(possibleParent, key, csvEntry.getAnnotations());
        entities.add(Entity);
        elements.put(key, Entity);
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
        matcher = patternWithCapture.matcher(description);
        return matcher.find();
    }

    private boolean isPreposition(String description) {
        return pattern.matcher(description).matches();
    }
}
