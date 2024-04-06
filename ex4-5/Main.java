import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.NodeList;
import java.util.Random;
import javax.xml.transform.OutputKeys;

public class Main {
    private static final String[] ETHNICITIES = {"HISPANIC", "ASIAN AND PACIFIC ISLANDER", "WHITE NON HISPANIC", "BLACK NON HISPANIC"};

    public static void main(String[] args) {
        try {
            Random random = new Random();
            String ethnicity = ETHNICITIES[random.nextInt(ETHNICITIES.length)];

            File inputFile = new File("Popular_Baby_Names_NY.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("row");

            Map<String, Integer> nameCountMap = new HashMap<>();
            Map<String, String> genderMap = new HashMap<>();
            Map<String, Integer> rankMap = new HashMap<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String name = element.getElementsByTagName("nm").item(0).getTextContent();
                String gender = element.getElementsByTagName("gndr").item(0).getTextContent();
                int count = Integer.parseInt(element.getElementsByTagName("cnt").item(0).getTextContent());
                int rank = Integer.parseInt(element.getElementsByTagName("rnk").item(0).getTextContent());
                String babyEthnicity = element.getElementsByTagName("ethcty").item(0).getTextContent();
                if (babyEthnicity.equalsIgnoreCase(ethnicity) && rank == 1) {
                    nameCountMap.put(name, nameCountMap.getOrDefault(name, 0) + count);
                    genderMap.put(name, gender);
                    rankMap.put(name, rank);
                }
            }
            saveUniqueRank1NamesToXML(nameCountMap, genderMap, rankMap);
            readFromXML(genderMap, ethnicity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveUniqueRank1NamesToXML(Map<String, Integer> nameCountMap, Map<String, String> genderMap, Map<String, Integer> rankMap) throws ParserConfigurationException {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("response");
            doc.appendChild(rootElement);

            for (String name : nameCountMap.keySet()) {
                int count = nameCountMap.get(name);
                String gender = genderMap.get(name);
                Integer rank = rankMap.get(name);

                if (rank != null && rank.intValue() == 1) {
                    Element rowElement = doc.createElement("row");
                    rootElement.appendChild(rowElement);

                    Element nameElement = doc.createElement("nm");
                    nameElement.appendChild(doc.createTextNode(name));
                    rowElement.appendChild(nameElement);

                    Element genderElement = doc.createElement("gndr");
                    genderElement.appendChild(doc.createTextNode(gender));
                    rowElement.appendChild(genderElement);

                    Element countElement = doc.createElement("cnt");
                    countElement.appendChild(doc.createTextNode(String.valueOf(count)));
                    rowElement.appendChild(countElement);

                    Element rankElement = doc.createElement("rnk");
                    rankElement.appendChild(doc.createTextNode(String.valueOf(rank.intValue())));
                    rowElement.appendChild(rankElement);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("Top_Rank_1_Baby_Names.xml"));
            transformer.transform(source, result);

            System.out.println("Saved unique names with rank 1 to file.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readFromXML(Map<String, String> genderMap, String ethnicity) {
        try {
            File inputFile = new File("Top_Rank_1_Baby_Names.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList rowList = doc.getElementsByTagName("row");

            System.out.println("Information read from XML file (Ethnicity: " + ethnicity + "):");
            for (int i = 0; i < rowList.getLength(); i++) {
                Element row = (Element) rowList.item(i);
                String name = row.getElementsByTagName("nm").item(0).getTextContent();
                int count = Integer.parseInt(row.getElementsByTagName("cnt").item(0).getTextContent());
                String gender = genderMap.get(name);
                int rank = Integer.parseInt(row.getElementsByTagName("rnk").item(0).getTextContent());

                System.out.println("Name: " + name + ", Gender: " + gender + ", Count: " + count + ", Rank: " + rank);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

