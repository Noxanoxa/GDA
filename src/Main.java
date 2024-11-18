import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        XMLReader xmlReader = new XMLReader();
        TextProcessor textProcessor = new TextProcessor();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the path of the directory containing XML files:");
        String directoryPath = scanner.nextLine();
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Invalid directory path: " + directoryPath);
            return;
        }

        File[] xmlFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
        if (xmlFiles == null || xmlFiles.length == 0) {
            System.err.println("No XML files found in the directory: " + directoryPath);
            return;
        }

        Map<String, Map<String, Integer>> fileWordIndex = new HashMap<>();
        Map<String, Set<String>> wordIndex = new HashMap<>();
        Map<String, List<String>> categoryMovies = new HashMap<>();
        List<String> fileNames = new ArrayList<>();

        processXMLFiles(xmlFiles, xmlReader, textProcessor, fileWordIndex, wordIndex, categoryMovies, fileNames);
        printFileWordIndex(fileWordIndex);
        printWordVectors(fileNames, wordIndex);
        printCategoryMovies(categoryMovies);
    }

    private static void processXMLFiles(File[] xmlFiles, XMLReader xmlReader, TextProcessor textProcessor,
                                        Map<String, Map<String, Integer>> fileWordIndex, Map<String, Set<String>> wordIndex,
                                        Map<String, List<String>> categoryMovies, List<String> fileNames) {
        for (File xmlFile : xmlFiles) {
            fileNames.add(xmlFile.getName());
            String textContent = xmlReader.readXMLFile(xmlFile.getAbsolutePath());
//            System.out.println("Text content for file " + xmlFile.getName() + ": " + textContent); // Debugging line
            Map<String, Integer> fileWordIndexMap = textProcessor.buildWordIndex(textContent);
            String category = xmlReader.extractCategoryFromXML(xmlFile.getAbsolutePath());
            String title = xmlReader.extractTitleFromXML(xmlFile.getAbsolutePath());
            String movieIdentifier = title + " (" + category + ")";
            fileWordIndex.put(xmlFile.getName(), fileWordIndexMap);
            categoryMovies.computeIfAbsent(category, k -> new ArrayList<>()).add(movieIdentifier);

            for (Map.Entry<String, Integer> entry : fileWordIndexMap.entrySet()) {
                wordIndex.computeIfAbsent(entry.getKey(), k -> new HashSet<>()).add(xmlFile.getName());
            }
        }
    }

    private static void printFileWordIndex(Map<String, Map<String, Integer>> fileWordIndex) {
        for (Map.Entry<String, Map<String, Integer>> entry : fileWordIndex.entrySet()) {
            System.out.println("Movie: " + entry.getKey());
            for (Map.Entry<String, Integer> wordEntry : entry.getValue().entrySet()) {
                System.out.println("  Word: " + wordEntry.getKey() + " - Count: " + wordEntry.getValue());
            }
        }
    }

    private static void printWordVectors(List<String> fileNames, Map<String, Set<String>> wordIndex) {
        Map<String, Map<String, Integer>> wordVectors = new HashMap<>();

        // Initialize the word vectors for each file
        for (String fileName : fileNames) {
            wordVectors.put(fileName, new HashMap<>());
        }

        // Populate the word vectors with the word counts
        for (String word : wordIndex.keySet()) {
            for (String fileName : fileNames) {
                if (wordIndex.get(word).contains(fileName)) {
                    wordVectors.get(fileName).put(word, 1);
                } else {
                    wordVectors.get(fileName).put(word, 0);
                }
            }
        }

        // Print the word vectors
        for (Map.Entry<String, Map<String, Integer>> entry : wordVectors.entrySet()) {
            System.out.println("File: " + entry.getKey() + " - Vector: " + entry.getValue());
        }
    }
    private static void printCategoryMovies(Map<String, List<String>> categoryMovies) {
        for (Map.Entry<String, List<String>> entry : categoryMovies.entrySet()) {
            System.out.println("Category: " + entry.getKey() + " - Movies: " + entry.getValue());
        }
    }
}
//D:\Series\films\xmlfiles\100_movies