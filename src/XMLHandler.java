import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to handle XML read/write operations.
 */
public class XMLHandler {
    private static final String USERS_FILE = "users.xml";
    private static final String PRODUCTS_FILE = "products.xml";

    /**
     * Reads users from the XML file.
     * @return A list of users.
     */
    public List<User> readUsers() {
        List<User> users = new ArrayList<>();
        try {
            File file = new File(USERS_FILE);
            if (!file.exists()) return users;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("user");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String username = eElement.getElementsByTagName("username").item(0).getTextContent();
                    String password = eElement.getElementsByTagName("password").item(0).getTextContent();
                    String role = eElement.getElementsByTagName("role").item(0).getTextContent();
                    User user = role.equals("admin") ? new Admin(username, password) : new User(username, password, role);

                    NodeList productNodes = eElement.getElementsByTagName("product");
                    for (int i = 0; i < productNodes.getLength(); i++) {
                        Element productElement = (Element) productNodes.item(i);
                        String id = productElement.getElementsByTagName("id").item(0).getTextContent();
                        String name = productElement.getElementsByTagName("name").item(0).getTextContent();
                        double price = Double.parseDouble(productElement.getElementsByTagName("price").item(0).getTextContent());
                        String userId = productElement.getElementsByTagName("userId").item(0).getTextContent();
                        user.getProducts().add(new Product(id, name, price, userId));
                    }

                    users.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Writes users to the XML file.
     * @param users The list of users to write.
     */
    public void writeUsers(List<User> users) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("users");
            doc.appendChild(rootElement);

            for (User user : users) {
                Element userElement = doc.createElement("user");
                rootElement.appendChild(userElement);

                Element username = doc.createElement("username");
                username.appendChild(doc.createTextNode(user.getUsername()));
                userElement.appendChild(username);

                Element password = doc.createElement("password");
                password.appendChild(doc.createTextNode(user.getPassword()));
                userElement.appendChild(password);

                Element role = doc.createElement("role");
                role.appendChild(doc.createTextNode(user.getRole()));
                userElement.appendChild(role);

                for (Product product : user.getProducts()) {
                    Element productElement = doc.createElement("product");
                    userElement.appendChild(productElement);

                    Element id = doc.createElement("id");
                    id.appendChild(doc.createTextNode(product.getId()));
                    productElement.appendChild(id);

                    Element name = doc.createElement("name");
                    name.appendChild(doc.createTextNode(product.getName()));
                    productElement.appendChild(name);

                    Element price = doc.createElement("price");
                    price.appendChild(doc.createTextNode(String.valueOf(product.getPrice())));
                    productElement.appendChild(price);

                    Element userId = doc.createElement("userId");
                    userId.appendChild(doc.createTextNode(product.getUserId()));
                    productElement.appendChild(userId);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(USERS_FILE));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads products from the XML file.
     * @return A list of products.
     */
    public List<Product> readProducts() {
        List<Product> products = new ArrayList<>();
        try {
            File file = new File(PRODUCTS_FILE);
            if (!file.exists()) return products;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("product");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode != null && nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String id = eElement.getElementsByTagName("id").item(0).getTextContent();
                    String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    double price = Double.parseDouble(eElement.getElementsByTagName("price").item(0).getTextContent());
                    Node userIdNode = eElement.getElementsByTagName("userId").item(0);
                    String userId = userIdNode != null ? userIdNode.getTextContent() : "";
                    products.add(new Product(id, name, price, userId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Writes products to the XML file.
     * @param products The list of products to write.
     */
    public void writeProducts(List<Product> products) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("products");
            doc.appendChild(rootElement);

            for (Product product : products) {
                Element productElement = doc.createElement("product");
                rootElement.appendChild(productElement);

                Element id = doc.createElement("id");
                id.appendChild(doc.createTextNode(product.getId()));
                productElement.appendChild(id);

                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(product.getName()));
                productElement.appendChild(name);

                Element price = doc.createElement("price");
                price.appendChild(doc.createTextNode(String.valueOf(product.getPrice())));
                productElement.appendChild(price);

                Element userId = doc.createElement("userId");
                userId.appendChild(doc.createTextNode(product.getUserId()));
                productElement.appendChild(userId);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(PRODUCTS_FILE));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}