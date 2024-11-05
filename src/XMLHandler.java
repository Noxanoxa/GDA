// XMLHandler.java
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLHandler {
    private static final String BIBLIOTHEQUE_FILE = "bibliotheque.xml";

    public List<Livre> readLivres() {
        List<Livre> livres = new ArrayList<>();
        try {
            File file = new File(BIBLIOTHEQUE_FILE);
            if (!file.exists()) return livres;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("livre");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String titre = eElement.getElementsByTagName("titre").item(0).getTextContent();
                    String isbn = eElement.getElementsByTagName("isbn").item(0).getTextContent();
                    String datePublication = eElement.getElementsByTagName("datePublication").item(0).getTextContent();
                    int idAuteur = Integer.parseInt(eElement.getElementsByTagName("idAuteur").item(0).getTextContent());
                    Livre livre = new Livre(titre, isbn, datePublication, idAuteur);
                    livres.add(livre);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return livres;
    }

    public List<Auteur> readAuteurs() {
        List<Auteur> auteurs = new ArrayList<>();
        try {
            File file = new File(BIBLIOTHEQUE_FILE);
            if (!file.exists()) return auteurs;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("auteur");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    int id = Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent());
                    String nom = eElement.getElementsByTagName("nom").item(0).getTextContent();
                    String nationalite = eElement.getElementsByTagName("nationalite").item(0).getTextContent();
                    auteurs.add(new Auteur(id, nom, nationalite));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return auteurs;
    }

    public List<Emprunt> readEmprunts() {
        List<Emprunt> emprunts = new ArrayList<>();
        try {
            File file = new File(BIBLIOTHEQUE_FILE);
            if (!file.exists()) {
                return emprunts;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("emprunt");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String userId = eElement.getElementsByTagName("userId").item(0).getTextContent();
                    String isbnLivre = eElement.getElementsByTagName("isbnLivre").item(0).getTextContent();
                    String dateEmprunt = eElement.getElementsByTagName("dateEmprunt").item(0).getTextContent();
                    String retourne = eElement.getElementsByTagName("retourne").item(0).getTextContent();
                    emprunts.add(new Emprunt(userId, isbnLivre, dateEmprunt, retourne));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emprunts;
    }

    public void writeLivres(List<Livre> livres) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("bibliotheque");
            doc.appendChild(rootElement);

            Element livresElement = doc.createElement("livres");
            rootElement.appendChild(livresElement);

            for (Livre livre : livres) {
                Element livreElement = doc.createElement("livre");
                livresElement.appendChild(livreElement);

                Element titre = doc.createElement("titre");
                titre.appendChild(doc.createTextNode(livre.getTitre()));
                livreElement.appendChild(titre);

                Element isbn = doc.createElement("isbn");
                isbn.appendChild(doc.createTextNode(livre.getIsbn()));
                livreElement.appendChild(isbn);

                Element datePublication = doc.createElement("datePublication");
                datePublication.appendChild(doc.createTextNode(livre.getDatePublication()));
                livreElement.appendChild(datePublication);

                Element idAuteur = doc.createElement("idAuteur");
                idAuteur.appendChild(doc.createTextNode(String.valueOf(livre.getIdAuteur())));
                livreElement.appendChild(idAuteur);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(BIBLIOTHEQUE_FILE));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeAuteurs(List<Auteur> auteurs) {
        try {
            File file = new File(BIBLIOTHEQUE_FILE);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc;

            if (file.exists()) {
                doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();
            } else {
                doc = dBuilder.newDocument();
                Element rootElement = doc.createElement("bibliotheque");
                doc.appendChild(rootElement);
            }

            Node rootElement = doc.getDocumentElement();
            NodeList auteursList = doc.getElementsByTagName("auteurs");
            Element auteursElement;

            if (auteursList.getLength() > 0) {
                auteursElement = (Element) auteursList.item(0);
                while (auteursElement.hasChildNodes()) {
                    auteursElement.removeChild(auteursElement.getFirstChild());
                }
            } else {
                auteursElement = doc.createElement("auteurs");
                rootElement.appendChild(auteursElement);
            }

            for (Auteur auteur : auteurs) {
                Element auteurElement = doc.createElement("auteur");
                auteursElement.appendChild(auteurElement);

                Element id = doc.createElement("id");
                id.appendChild(doc.createTextNode(String.valueOf(auteur.getId())));
                auteurElement.appendChild(id);

                Element nom = doc.createElement("nom");
                nom.appendChild(doc.createTextNode(auteur.getNom()));
                auteurElement.appendChild(nom);

                Element nationalite = doc.createElement("nationalite");
                nationalite.appendChild(doc.createTextNode(auteur.getNationalite()));
                auteurElement.appendChild(nationalite);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeEmprunts(List<Emprunt> emprunts) {
        try {
            File file = new File(BIBLIOTHEQUE_FILE);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc;

            if (file.exists()) {
                doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();
            } else {
                doc = dBuilder.newDocument();
                Element rootElement = doc.createElement("bibliotheque");
                doc.appendChild(rootElement);
            }

            Node rootElement = doc.getDocumentElement();
            NodeList empruntsList = doc.getElementsByTagName("emprunts");
            Element empruntsElement;

            if (empruntsList.getLength() > 0) {
                empruntsElement = (Element) empruntsList.item(0);
                while (empruntsElement.hasChildNodes()) {
                    empruntsElement.removeChild(empruntsElement.getFirstChild());
                }
            } else {
                empruntsElement = doc.createElement("emprunts");
                rootElement.appendChild(empruntsElement);
            }

            for (Emprunt emprunt : emprunts) {
                Element empruntElement = doc.createElement("emprunt");
                empruntsElement.appendChild(empruntElement);

                Element userId = doc.createElement("userId");
                userId.appendChild(doc.createTextNode(emprunt.getUserId()));
                empruntElement.appendChild(userId);

                Element isbnLivre = doc.createElement("isbnLivre");
                isbnLivre.appendChild(doc.createTextNode(emprunt.getIsbnLivre()));
                empruntElement.appendChild(isbnLivre);

                Element dateEmprunt = doc.createElement("dateEmprunt");
                dateEmprunt.appendChild(doc.createTextNode(emprunt.getDateEmprunt()));
                empruntElement.appendChild(dateEmprunt);

                Element retourne = doc.createElement("retourne");
                retourne.appendChild(doc.createTextNode(emprunt.getRetourne()));
                empruntElement.appendChild(retourne);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

