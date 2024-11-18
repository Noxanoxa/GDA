import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XMLReader {

    public String readXMLFile(String filePath) {
        StringBuilder textContent = new StringBuilder();
        Document doc = parseXMLFile(filePath);
        if (doc == null) {
            return "";
        }
        NodeList titleList = doc.getElementsByTagName("title");
        if (titleList.getLength() > 0) {
            textContent.append(titleList.item(0).getTextContent().trim()).append(" ");
        }

        NodeList directorList = doc.getElementsByTagName("director");
        if (directorList.getLength() > 0) {
            textContent.append(directorList.item(0).getTextContent().trim()).append(" ");
        }

        NodeList yearList = doc.getElementsByTagName("year");
        if (yearList.getLength() > 0) {
            textContent.append(yearList.item(0).getTextContent().trim()).append(" ");
        }

        NodeList categoryList = doc.getElementsByTagName("category");
        if (categoryList.getLength() > 0) {
            textContent.append(categoryList.item(0).getTextContent().trim()).append(" ");
        }

        return textContent.toString().trim();
    }

    public String extractCategoryFromXML(String filePath) {
        Document doc = parseXMLFile(filePath);
        if (doc == null) {
            return "default_category";
        }
        NodeList nodeList = doc.getElementsByTagName("category");

        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent().trim();
        }
        return "default_category";
    }

    public String extractTitleFromXML(String filePath) {
        Document doc = parseXMLFile(filePath);
        if (doc == null) {
            return "unknown_title";
        }
        NodeList nodeList = doc.getElementsByTagName("title");

        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent().trim();
        }
        return "unknown_title";
    }

    private Document parseXMLFile(String filePath) {
        try {
            File xmlFile = new File(filePath);
            if (!xmlFile.exists() || !xmlFile.isFile()) {
                System.err.println("File not found or is not a file: " + filePath);
                return null;
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception e) {
            System.err.println("Error parsing file: " + filePath);
            e.printStackTrace();
            return null;
        }
    }
}