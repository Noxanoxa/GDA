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

        Map<String, Set<String>> wordIndex = new HashMap<>();

        for (File xmlFile : xmlFiles) {
            String textContent = xmlReader.readXMLFile(xmlFile.getAbsolutePath());
            Map<String, Integer> fileWordIndex = textProcessor.buildWordIndex(textContent);

            for (String word : fileWordIndex.keySet()) {
                wordIndex.computeIfAbsent(word, k -> new HashSet<>()).add(xmlFile.getName());
            }
        }

        for (Map.Entry<String, Set<String>> entry : wordIndex.entrySet()) {
            System.out.println("Word: " + entry.getKey() + " - Files: " + entry.getValue());
        }
        List<String> fileNames = new ArrayList<>();

        for (File xmlFile : xmlFiles) {
            fileNames.add(xmlFile.getName());
            String textContent = xmlReader.readXMLFile(xmlFile.getAbsolutePath());
            Map<String, Integer> fileWordIndex = textProcessor.buildWordIndex(textContent);

            for (String word : fileWordIndex.keySet()) {
                wordIndex.computeIfAbsent(word, k -> new HashSet<>()).add(xmlFile.getName());
            }
        }

        Map<String, Map<String, Integer>> wordVectors = new HashMap<>();

        for (String fileName : fileNames) {
            wordVectors.put(fileName, new HashMap<>());
        }

        for (String word : wordIndex.keySet()) {
            for (String fileName : fileNames) {
                wordVectors.get(fileName).put(word, wordIndex.get(word).contains(fileName) ? 1 : 0);
            }
        }

        for (Map.Entry<String, Map<String, Integer>> entry : wordVectors.entrySet()) {
            System.out.println("File: " + entry.getKey() + " - Vector: " + entry.getValue());
        }

    }
}