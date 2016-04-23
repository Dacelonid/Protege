package ie.dacelonid.CSVParser;

import ie.dacelonid.ontology.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVEntryToEntity {

    private static final String PREPOSITION_REGEX = "(above|at|but|for|in|of|over|with||without)";
    private static final String PREPOSITION_REGEX_WITH_CAPTURE = "(.*?)\\s+" + PREPOSITION_REGEX + "\\s+(.*)";
    private static final Pattern patternWithCapture = Pattern.compile(PREPOSITION_REGEX_WITH_CAPTURE, Pattern.CASE_INSENSITIVE);
    private static final Pattern pattern = Pattern.compile(PREPOSITION_REGEX, Pattern.CASE_INSENSITIVE);
    private static Map<String, Integer> method_count = new HashMap<>();
    private final Map<String, Thing> elements = new HashMap<>();
    private final Thing annotation_Entity = new Annotation("annotations");
    private final Thing classes_Entity = new Entity("classes");
    private Matcher matcher;
    private List<RdfElement> rdfElements;

    public void convert(List<CSVEntry> itemsToConvert) {
        rdfElements = new ArrayList<>();

        addTopLevelElements();

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

    private void addTopLevelElements() {
        rdfElements.add(annotation_Entity);
        rdfElements.add(classes_Entity);
        rdfElements.add(new DisjointClasses(annotation_Entity.getName(), classes_Entity.getName()));
        Property property = new Property.PropertyBuilder().name("has_annotation").functional().domain(classes_Entity).range(annotation_Entity).build();
        rdfElements.add(property);
    }

    private List<Thing> processAnnotations(CSVEntry csvEntry) {
        List<Thing> newEntities = new ArrayList<>();
        for (String annotation : csvEntry.getAnnotations()) {
            if (elements.containsKey(annotation + "_annotation")) {
                continue;
            }
            Thing newEntity = new Annotation(annotation_Entity, annotation);
            newEntities.add(newEntity);
            elements.put(annotation + "_annotation", newEntity);
        }
        return newEntities;
    }

    private List<Thing> processSpecialCases(CSVEntry csvEntry) {
        Thing parent = getParent(csvEntry);
        Thing subClasses = new Entity(parent, csvEntry.getDescription(), csvEntry.getAnnotations());
        elements.put(csvEntry.getDescription(), subClasses);
        return Arrays.asList(parent, subClasses);
    }

    private Thing getParent(CSVEntry csvEntry) {
        if ("text".equals(csvEntry.getNature())) {
            return new Entity("text");
        } else {
            return new Entity(classes_Entity, "ideograph", Collections.emptyList());
        }

    }

    private boolean descriptionNeedsSpecialHandling(CSVEntry nature) {
        return "text".equals(nature.getNature()) || nature.getDescription().contains("ideograph");
    }

    private List<Thing> processDescription(CSVEntry csvEntry) {
        if (elements.containsKey(csvEntry.getDescription())) {
            return Collections.emptyList();
        }
        List<Thing> entities = new ArrayList<>();
        Thing parent;
        if (hasPreposition(csvEntry.getDescription())) {
            parent = getParent(getLeftHandSideOfPreposition());
        } else {
            parent = getParent1(csvEntry.getDescription());
        }
        Thing Entity = new Entity(parent, csvEntry.getDescription(), csvEntry.getAnnotations());
        entities.add(Entity);
        elements.put(csvEntry.getDescription(), Entity);
        return entities;

    }

    private Thing getParent1(String description) {
        if (hasMultipleWordParent(description)) {
            return getMultipleWordParent(description);
        }
        String lastWord = getLastWord(description);
        if (elements.containsKey(lastWord)) {
            return elements.get(lastWord);
        } else {
            return new NullEntity();
        }
    }

    private Thing getParent(String parent) {
        if (hasMultipleWordParent(parent)) {
            return getMultipleWordParent(parent);
        }
        return elements.get(parent);
    }

    private Thing getMultipleWordParent(String parent) {
        if (elements.containsKey(parent)) {
            return elements.get(parent);
        }
        return getParentAfterRemovingFirstWord(parent);
    }

    private Thing getParentAfterRemovingFirstWord(String parent) {
        String[] splitWords = parent.split(" ", 2);
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

    private List<Thing> processIndividualWords(String descriptions, CSVEntry csvEntry) {
        List<Thing> lineEntities = new ArrayList<>();
        String lastWord = getLastWord(descriptions);
        if (!isPreposition(lastWord)) {
            if (!elements.containsKey(lastWord)) {
                final Entity thing = new Entity(classes_Entity, lastWord, getMatchingAnnotations(lastWord, csvEntry));
                lineEntities.add(thing);
                elements.put(lastWord, thing);
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

    private List<Thing> processCompoundWords(CSVEntry csvEntry) {
        String description = csvEntry.getDescription();
        if (hasPreposition(description)) {
            return handlePrepositions(csvEntry);
        } else {
            return processIndividualWords(description, csvEntry);
        }
    }

    private List<Thing> handlePrepositions(CSVEntry csvEntry) {
        List<Thing> entities = new ArrayList<>();
        String key1 = getLeftHandSideOfPreposition();
        entities.addAll(processIndividualWords(key1, csvEntry));
        entities.addAll(handleElementOfPreposition(key1, csvEntry));
        return entities;
    }

    private List<Thing> handleElementOfPreposition(String key, CSVEntry csvEntry) {
        List<Thing> entities = new ArrayList<>();
        if (elements.containsKey(key)) {
            return Collections.emptyList();
        }
        Thing possibleParent = getPossibleParent(key);
        Thing Entity = new Entity(possibleParent, key, csvEntry.getAnnotations());
        entities.add(Entity);
        elements.put(key, Entity);
        return entities;
    }

    private Thing getPossibleParent(String key) {
        for (String keyPart : key.split(" ")) {
            if (elements.containsKey(keyPart)) {
                return elements.get(keyPart);
            }
        }
        return new NullEntity();
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

    public List<RdfElement> getProperties() {
        return rdfElements;
    }
}
