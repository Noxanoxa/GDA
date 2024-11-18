import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class XMLFileGenerator {
    private static final String[] TITLES = {"Inception", "The Godfather", "The Dark Knight", "Pulp Fiction", "Forrest Gump"};
    private static final String[] DIRECTORS = {"Christopher Nolan", "Francis Ford Coppola", "Quentin Tarantino", "Steven Spielberg", "Martin Scorsese"};
    private static final String[] CATEGORIES = {"Science Fiction", "Crime", "Drama", "Action", "Comedy"};
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        String directoryPath = "D:\\Series\\films\\xmlfiles\\Movies";
        int numberOfFiles = 10000;

        for (int i = 0; i < numberOfFiles; i++) {
            String xmlContent = generateXMLContent();
            writeXMLFile(directoryPath, "movie_" + i + ".xml", xmlContent);
        }
    }

    private static String generateXMLContent() {
        String title = TITLES[RANDOM.nextInt(TITLES.length)];
        String director = DIRECTORS[RANDOM.nextInt(DIRECTORS.length)];
        int year = 1980 + RANDOM.nextInt(41); // Random year between 1980 and 2020
        String category = CATEGORIES[RANDOM.nextInt(CATEGORIES.length)];

        return "<library>\n" +
                "    <movie>\n" +
                "        <title>" + title + "</title>\n" +
                "        <director>" + director + "</director>\n" +
                "        <year>" + year + "</year>\n" +
                "        <category>" + category + "</category>\n" +
                "    </movie>\n" +
                "</library>";
    }

    private static void writeXMLFile(String directoryPath, String fileName, String content) {
        File file = new File(directoryPath, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}