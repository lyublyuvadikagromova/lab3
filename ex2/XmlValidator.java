import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class XmlValidator {
    public static void main(String[] args) {
        try {

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File("schema.xsd"));


            Validator validator = schema.newValidator();


            validator.validate(new StreamSource(new File("Popular_Baby_Names_NY.xml")));

            System.out.println("XML документ відповідає XSD схемі.");
        } catch (SAXException e) {
            System.out.println("XML документ не відповідає XSD схемі:");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

