import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;

public class MainGUI extends JFrame {
    private JTextField directoryPathField;
    private JTextArea outputArea;

    public MainGUI() {
        setTitle("XML File Processor");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel directoryLabel = new JLabel("Directory Path:");
        inputPanel.add(directoryLabel);

        directoryPathField = new JTextField(30);
        inputPanel.add(directoryPathField);

        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(new BrowseButtonListener());
        inputPanel.add(browseButton);

        JButton processButton = new JButton("Process");
        processButton.addActionListener(new ProcessButtonListener());
        inputPanel.add(processButton);

        add(inputPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
    }

    private class BrowseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(MainGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedDirectory = fileChooser.getSelectedFile();
                directoryPathField.setText(selectedDirectory.getAbsolutePath());
            }
        }
    }

    private class ProcessButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String directoryPath = directoryPathField.getText();
            File directory = new File(directoryPath);

            if (!directory.exists() || !directory.isDirectory()) {
                JOptionPane.showMessageDialog(MainGUI.this, "Invalid directory path", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            File[] xmlFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
            if (xmlFiles == null || xmlFiles.length == 0) {
                JOptionPane.showMessageDialog(MainGUI.this, "No XML files found in the directory", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            XMLReader xmlReader = new XMLReader();
            TextProcessor textProcessor = new TextProcessor();
            Map<String, Map<String, Integer>> fileWordIndex = new HashMap<>();
            Map<String, Set<String>> wordIndex = new HashMap<>();
            Map<String, List<String>> categoryMovies = new HashMap<>();
            List<String> fileNames = new ArrayList<>();

            processXMLFiles(xmlFiles, xmlReader, textProcessor, fileWordIndex, wordIndex, categoryMovies, fileNames);
            displayResults(fileWordIndex, wordIndex, categoryMovies);
        }
    }

    private void processXMLFiles(File[] xmlFiles, XMLReader xmlReader, TextProcessor textProcessor,
                                 Map<String, Map<String, Integer>> fileWordIndex, Map<String, Set<String>> wordIndex,
                                 Map<String, List<String>> categoryMovies, List<String> fileNames) {
        for (File xmlFile : xmlFiles) {
            fileNames.add(xmlFile.getName());
            String textContent = xmlReader.readXMLFile(xmlFile.getAbsolutePath());
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

    private void displayResults(Map<String, Map<String, Integer>> fileWordIndex, Map<String, Set<String>> wordIndex,
                                Map<String, List<String>> categoryMovies) {
        StringBuilder results = new StringBuilder();

        results.append("File Word Index:\n");
        for (Map.Entry<String, Map<String, Integer>> entry : fileWordIndex.entrySet()) {
            results.append("Movie: ").append(entry.getKey()).append("\n");
            for (Map.Entry<String, Integer> wordEntry : entry.getValue().entrySet()) {
                results.append("  Word: ").append(wordEntry.getKey()).append(" - Count: ").append(wordEntry.getValue()).append("\n");
            }
        }

        results.append("\nWord Vectors:\n");
        Map<String, Map<String, Integer>> wordVectors = new HashMap<>();
        for (String fileName : wordIndex.keySet()) {
            wordVectors.put(fileName, new HashMap<>());
        }
        for (String word : wordIndex.keySet()) {
            for (String fileName : wordIndex.keySet()) {
                if (wordIndex.get(word).contains(fileName)) {
                    wordVectors.get(fileName).put(word, 1);
                } else {
                    wordVectors.get(fileName).put(word, 0);
                }
            }
        }
        for (Map.Entry<String, Map<String, Integer>> entry : wordVectors.entrySet()) {
            results.append("File: ").append(entry.getKey()).append(" - Vector: ").append(entry.getValue()).append("\n");
        }

        results.append("\nCategory Movies:\n");
        for (Map.Entry<String, List<String>> entry : categoryMovies.entrySet()) {
            results.append("Category: ").append(entry.getKey()).append(" - Movies: ").append(entry.getValue()).append("\n");
        }

        outputArea.setText(results.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI mainGUI = new MainGUI();
            mainGUI.setVisible(true);
        });
    }
}