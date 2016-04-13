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

    private Entity annotation_Entity = new Entity("annotations");
    private Entity classes_Entity = new Entity("classes");
    private List<RdfElement> rdfElements;
    private List<Entity> entities;
    private List<RdfElement> disjointClasses;
    private List<Entity> properties;

    public void convert(List<CSVEntry> itemsToConvert){
        entities = new ArrayList<>();
        disjointClasses = new ArrayList<>();
        properties = new ArrayList<>();
        rdfElements = new ArrayList<>();

        entities.add(annotation_Entity);
        entities.add(classes_Entity);
        disjointClasses.add(new DisjointClasses(annotation_Entity.getName(), classes_Entity.getName()));
        rdfElements.add(new Property("has_annotation"));

        for (CSVEntry csvEntry : itemsToConvert) {
            entities.addAll(processAnnotations(csvEntry));
            if (descriptionNeedsSpecialHandling(csvEntry)) {
                entities.addAll(processSpecialCases(csvEntry));
            } else {
                entities.addAll(processCompoundWords(csvEntry));
                entities.addAll(processDescription(csvEntry));
            }
        }
    }

    List<Entity> getEntities() {
        return entities;
    }

    List<RdfElement> getProperties(){
        return rdfElements;
    }

    private List<Entity> processAnnotations(CSVEntry csvEntry) {
        List<Entity> newEntities = new ArrayList<>();
        for(String annotation: csvEntry.getAnnotations()){
            if(elements.containsKey(annotation+"_annotation")){
                continue;
            }
            Entity newEntity = new Entity(annotation_Entity, annotation + "_annotation");
            newEntities.add(newEntity);
            elements.put(annotation+"_annotation", newEntity);
        }
        return  newEntities;
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
        Entity Entity = new Entity(parent, csvEntry.getDescription());
        entities.add(Entity);
        elements.put(csvEntry.getDescription(), Entity);
        return entities;

    }

    private List<Entity> processIndividualWords(String descriptions) {
        List<Entity> lineEntities = new ArrayList<>();
        String lastWord = getLastWord(descriptions);
        if (!isPreposition(lastWord)) {
            if (!elements.containsKey(lastWord)) {
                final Entity Entity = new Entity(classes_Entity, lastWord);
                    lineEntities.add(Entity);
                elements.put(lastWord, Entity);
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
        Entity Entity = new Entity(possibleParent, key);
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
        matcher = pattern.matcher(description);
        return matcher.find();
    }

    private boolean isPreposition(String description) {
        return pattern1.matcher(description).matches();
    }

    public List<RdfElement> getDisjointClasses() {
        return disjointClasses;
    }
}
