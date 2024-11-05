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
    }
}