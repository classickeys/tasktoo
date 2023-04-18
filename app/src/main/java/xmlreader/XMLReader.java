package xmlreader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XMLReader {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java XMLReader data.xml name,postalZip,region,country,address,list");
            return;
        }

        String filename = args[0];
        String[] fields = args[1].split(",");

        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            UserHandler userHandler = new UserHandler(fields);
            saxParser.parse(new File(filename), userHandler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private static class UserHandler extends DefaultHandler {
        private List<String> selectedFields;
        private String currentField;
        private StringBuilder currentText;

        public UserHandler(String[] fields) {
            selectedFields = new ArrayList<>();
            for (String field : fields) {
                selectedFields.add(field.trim());
            }
            currentText = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            currentField = qName;
            currentText.setLength(0);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (selectedFields.contains(qName)) {
                System.out.println(qName + ": " + currentText.toString().trim());
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            currentText.append(ch, start, length);
        }
    }
}
