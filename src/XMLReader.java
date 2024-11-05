import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XMLReader {
    public String readXMLFile(String filePath) {
        StringBuilder textContent = new StringBuilder();
        try {
            File xmlFile = new File(filePath);
            if (!xmlFile.exists() || !xmlFile.isFile()) {
                System.err.println("File not found or is not a file: " + filePath);
                return "";
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("*");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    textContent.append(node.getTextContent()).append(" ");
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing file: " + filePath);
            e.printStackTrace();
        }
        return textContent.toString().trim();
    }
}