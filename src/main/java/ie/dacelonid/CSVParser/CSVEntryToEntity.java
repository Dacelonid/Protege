package ie.dacelonid.CSVParser;

import ie.dacelonid.ontology.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ie.dacelonid.ontology.Annotation.getAnnotationName;

public class CSVEntryToEntity {

    private static final String PREPOSITION_REGEX = "(above|at|but|for|in|of|over|with||without)";
    private static final String PREPOSITION_REGEX_WITH_CAPTURE = "(.*?)\\s+" + PREPOSITION_REGEX + "\\s+(.*)";
    private static final Pattern patternWithCapture = Pattern.compile(PREPOSITION_REGEX_WITH_CAPTURE, Pattern.CASE_INSENSITIVE);
    private static final Pattern pattern = Pattern.compile(PREPOSITION_REGEX, Pattern.CASE_INSENSITIVE);
    private static final String SPACE = " ";

    private static Map<String, Integer> method_count = new HashMap<>();
    private final Map<String, Thing> alreadyExistingElements = new HashMap<>();
    private final Thing annotationEntity = new Annotation("annotations");
    private final Thing classesEntity = new Entity("classes");
    private Matcher matcher;
    private List<RdfElement> rdfElements;
    private DisjointClasses disjointClasses = new DisjointClasses(annotationEntity.getName(), classesEntity.getName());

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
        rdfElements.add(annotationEntity);
        rdfElements.add(classesEntity);
        rdfElements.add(disjointClasses);
        Property property = new Property.PropertyBuilder().name("has_annotation").functional().domain(classesEntity).range(annotationEntity).build();
        rdfElements.add(property);
    }

    private List<Thing> processAnnotations(CSVEntry csvEntry) {
        List<Thing> newAnnotations = new ArrayList<>();
        for (String annotation : csvEntry.getAnnotations()) {
            if (alreadyExistingElements.containsKey(getAnnotationName(annotation))) {
                continue;
            }
            Thing newAnnotation = new Annotation(annotationEntity, annotation);
            newAnnotations.add(newAnnotation);
            alreadyExistingElements.put(getAnnotationName(annotation), newAnnotation);
        }
        return newAnnotations;
    }

    private boolean descriptionNeedsSpecialHandling(CSVEntry nature) {
        return "text".equals(nature.getNature()) || nature.getDescription().contains("ideograph");
    }

    private List<Thing> processSpecialCases(CSVEntry csvEntry) {
        Thing parent = getParentFromPreposition(csvEntry);
        Thing subClasses = new Entity(parent, csvEntry.getDescription(), csvEntry.getAnnotations());
        alreadyExistingElements.put(csvEntry.getDescription(), subClasses);
        return Arrays.asList(parent, subClasses);
    }

    private Thing getParentFromPreposition(CSVEntry csvEntry) {
        if ("text".equals(csvEntry.getNature())) {
            return new Entity("text");
        } else {
            return new Entity(classesEntity, "ideograph", Collections.emptyList());
        }
    }

    private List<Thing> processCompoundWords(CSVEntry csvEntry) {
        String description = csvEntry.getDescription();
        if (hasPreposition(description)) {
            return handlePrepositions(csvEntry);
        } else {
            return processIndividualWords(description, csvEntry);
        }
    }

    private boolean hasPreposition(String description) {
        matcher = patternWithCapture.matcher(description);
        return matcher.find();
    }

    private List<Thing> handlePrepositions(CSVEntry csvEntry) {
        List<Thing> entities = new ArrayList<>();
        String key1 = getLeftHandSideOfPreposition();
        entities.addAll(processIndividualWords(key1, csvEntry));
        entities.addAll(handleElementOfPreposition(key1, csvEntry));
        return entities;
    }

    private String getLeftHandSideOfPreposition() {
        return matcher.group(1);
    }

    private List<Thing> processIndividualWords(String descriptions, CSVEntry csvEntry) {
        List<Thing> lineEntities = new ArrayList<>();
        String lastWord = getLastWord(descriptions);
        if (!isPreposition(lastWord)) {
            if (!alreadyExistingElements.containsKey(lastWord)) {
                final Entity entity = new Entity(classesEntity, lastWord, getMatchingAnnotations(lastWord, csvEntry));
                lineEntities.add(entity);
                alreadyExistingElements.put(lastWord, entity);
            }
        }
        return lineEntities;
    }

    private String getLastWord(String descriptions) {
        return descriptions.replaceAll("^.*?(\\w+)\\W*$", "$1");
    }

    private boolean isPreposition(String description) {
        return pattern.matcher(description).matches();
    }

    private List<Thing> processDescription(CSVEntry csvEntry) {
        if (alreadyExistingElements.containsKey(csvEntry.getDescription())) {
            return Collections.emptyList();
        }
        Thing parent = getParent(csvEntry);
        Thing entity = new Entity(parent, csvEntry.getDescription(), csvEntry.getAnnotations());
        alreadyExistingElements.put(csvEntry.getDescription(), entity);
        return Collections.singletonList(entity);
    }

    private Thing getParent(CSVEntry csvEntry) {
        if (hasPreposition(csvEntry.getDescription())) {
            return getParentFromPreposition(getLeftHandSideOfPreposition());
        }
        return getParentUsingFullDescription(csvEntry.getDescription());
    }

    private Thing getParentFromPreposition(String description) {
        if (hasMultipleWordParent(description)) {
            return getMultipleWordParent(description);
        }
        return alreadyExistingElements.get(description);
    }

    private Thing getParentUsingFullDescription(String description) {
        if (hasMultipleWordParent(description)) {
            return getMultipleWordParent(description);
        }
        String lastWord = getLastWord(description);
        if (alreadyExistingElements.containsKey(lastWord)) {
            return alreadyExistingElements.get(lastWord);
        }
        return new NullEntity();
    }

    private Thing getMultipleWordParent(String parent) {
        if (alreadyExistingElements.containsKey(parent)) {
            return alreadyExistingElements.get(parent);
        }
        return getParentAfterRemovingFirstWord(parent);
    }

    private Thing getParentAfterRemovingFirstWord(String parent) {
        String[] splitWords = parent.split(SPACE, 2);
        while (splitWords.length == 2) {
            if (alreadyExistingElements.containsKey(splitWords[1])) {
                return alreadyExistingElements.get(splitWords[1]);
            } else {
                splitWords = splitWords[1].split(SPACE, 2);
            }
        }
        return alreadyExistingElements.get(splitWords[0]);
    }

    private boolean hasMultipleWordParent(String leftHandSideOfPreposition) {
        if (alreadyExistingElements.containsKey(leftHandSideOfPreposition)) {
            return true;
        }
        String[] headAndTail = leftHandSideOfPreposition.split(SPACE, 2);
        while (headAndTail.length == 2) {
            String tail = headAndTail[1];
            if (alreadyExistingElements.containsKey(tail)) {
                return true;
            }
            headAndTail = tail.split(SPACE, 2);
        }
        return false;
    }


    private List<String> getMatchingAnnotations(String name, CSVEntry csvEntry) {
        for (String annotation : csvEntry.getAnnotations()) {
            if (annotation.equals(name)) {
                return Collections.singletonList(name);
            }
        }
        return csvEntry.getAnnotations();
    }


    private List<Thing> handleElementOfPreposition(String key, CSVEntry csvEntry) {
        if (alreadyExistingElements.containsKey(key)) {
            return Collections.emptyList();
        }
        Thing possibleParent = getParentFromIndividualWords(key);
        Thing entity = new Entity(possibleParent, key, csvEntry.getAnnotations());
        alreadyExistingElements.put(key, entity);
        return Collections.singletonList(entity);
    }

    private Thing getParentFromIndividualWords(String key) {
        for (String keyPart : key.split(SPACE)) {
            if (alreadyExistingElements.containsKey(keyPart)) {
                return alreadyExistingElements.get(keyPart);
            }
        }
        return new NullEntity();
    }

    public List<RdfElement> getElements() {
        return rdfElements;
    }
}
