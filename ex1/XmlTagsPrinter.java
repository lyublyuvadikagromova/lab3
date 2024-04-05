import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class XmlTagsPrinter extends DefaultHandler {
    private Set<String> tags = new HashSet<>();

    public void parseXml(String xmlFilePath) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(xmlFilePath, this);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tags.add(qName);
    }

    public Set<String> getTags() {
        return tags;
    }

    public static void main(String[] args) {
        try {
            XmlTagsPrinter xmlTagsPrinter = new XmlTagsPrinter();
            xmlTagsPrinter.parseXml("Popular_Baby_Names_NY.xml");
            Set<String> tags = xmlTagsPrinter.getTags();
            System.out.println("Список тегів у файлі:");
            for (String tag : tags) {
                System.out.println(tag);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
