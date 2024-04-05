import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class NationalityParser {

    public static void main(String[] args) {
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();


            SAXParser saxParser = factory.newSAXParser();


            File xmlFile = new File("Popular_Baby_Names_NY.xml");


            NationalityHandler handler = new NationalityHandler();


            saxParser.parse(xmlFile, handler);


            Set<String> nationalities = handler.getNationalities();


            System.out.println("Назви національних груп:");

            for (String nationality : nationalities) {
                System.out.println(nationality);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }


    private static class NationalityHandler extends DefaultHandler {
        private Set<String> nationalities = new HashSet<>();
        private boolean isEthnicity = false;


        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            if (qName.equalsIgnoreCase("ethcty")) {
                isEthnicity = true;
            }
        }


        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            
            if (isEthnicity) {
                nationalities.add(new String(ch, start, length));
                isEthnicity = false;
            }
        }


        public Set<String> getNationalities() {
            return nationalities;
        }
    }
}
