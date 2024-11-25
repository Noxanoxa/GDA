import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;

public class MainGUI2 extends JFrame {
    private JTextField directoryPathField;
    private JTable wordIndexTable;
    private JTable wordVectorTable;
    private JTable categoryMoviesTable;

    public MainGUI2() {
        setTitle("XML File Processor");
        setSize(800, 600);
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

        JTabbedPane tabbedPane = new JTabbedPane();

        wordIndexTable = new JTable();
        JScrollPane wordIndexScrollPane = new JScrollPane(wordIndexTable);
        wordIndexScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabbedPane.addTab("Word Index", new JScrollPane(wordIndexTable));

        wordVectorTable = new JTable();
        JScrollPane wordVectorScrollPane = new JScrollPane(wordVectorTable);
        wordVectorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabbedPane.addTab("Word Vectors", new JScrollPane(wordVectorTable));

        categoryMoviesTable = new JTable();
        JScrollPane categoryMoviesScrollPane = new JScrollPane(categoryMoviesTable);
        categoryMoviesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabbedPane.addTab("Category Movies", new JScrollPane(categoryMoviesTable));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private class BrowseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(MainGUI2.this);
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
                JOptionPane.showMessageDialog(MainGUI2.this, "Invalid directory path", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            File[] xmlFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
            if (xmlFiles == null || xmlFiles.length == 0) {
                JOptionPane.showMessageDialog(MainGUI2.this, "No XML files found in the directory", "Error", JOptionPane.ERROR_MESSAGE);
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
        displayWordIndex(fileWordIndex);
        displayWordVectors(wordIndex);
        displayCategoryMovies(categoryMovies);
    }

    private void displayWordIndex(Map<String, Map<String, Integer>> fileWordIndex) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Movie", "Word", "Count"}, 0);
        for (Map.Entry<String, Map<String, Integer>> entry : fileWordIndex.entrySet()) {
            String movie = entry.getKey();
            for (Map.Entry<String, Integer> wordEntry : entry.getValue().entrySet()) {
                model.addRow(new Object[]{movie, wordEntry.getKey(), wordEntry.getValue()});
            }
        }
        wordIndexTable.setModel(model);
    }

    private void displayWordVectors(Map<String, Set<String>> wordIndex) {
        Map<String, Map<String, Integer>> wordVectors = new HashMap<>();

        // Initialize the word vectors for each file
        for (String fileName : wordIndex.keySet()) {
            wordVectors.put(fileName, new HashMap<>());
        }

        // Populate the word vectors with the word counts
        for (String word : wordIndex.keySet()) {
            for (String fileName : wordVectors.keySet()) {
                wordVectors.get(fileName).put(word, wordIndex.get(word).contains(fileName) ? 1 : 0);
            }
        }

        // Create table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("File");
        model.addColumn("Vector");

        // Add rows to the table model
        for (Map.Entry<String, Map<String, Integer>> entry : wordVectors.entrySet()) {
            String fileName = entry.getKey();
            String vector = entry.getValue().toString();
            model.addRow(new Object[]{fileName, vector});
        }

        // Set the model to the table
        wordVectorTable.setModel(model);
    }
    private void displayCategoryMovies(Map<String, List<String>> categoryMovies) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Category", "Movies"}, 0);
        for (Map.Entry<String, List<String>> entry : categoryMovies.entrySet()) {
            model.addRow(new Object[]{entry.getKey(), String.join(", ", entry.getValue())});
        }
        categoryMoviesTable.setModel(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI2 MainGUI2 = new MainGUI2();
            MainGUI2.setVisible(true);
        });
    }
}
//D:\Series\films\xmlfiles\100_movies
