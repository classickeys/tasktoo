package xmlreader;

import java.io.File;
import java.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class XMLReader {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter path of XML file: ");
        String xmlFilePath = scanner.nextLine();

        File xmlFile = new File(xmlFilePath);
        if (!xmlFile.exists()) {
            System.out.println("File not found: " + xmlFilePath);
            scanner.close();
            return;
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            System.out.print("Enter field names (comma-separated): ");
            String fieldNamesString = scanner.nextLine();
            List<String> fields = Arrays.asList(fieldNamesString.split(","));

            Map<String, String> fieldValues = extractFieldValues(doc, fields);

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String json = objectMapper.writeValueAsString(fieldValues);
                System.out.println(json);
            } catch (JsonProcessingException e) {
                System.out.println("Error generating JSON: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Error reading XML file: " + e.getMessage());
        }

        scanner.close();
    }

    private static Map<String, String> extractFieldValues(Document doc, List<String> fields) {
        Map<String, String> fieldValues = new HashMap<>();
        Element root = doc.getDocumentElement();

        for (String field : fields) {
            // Check if the field exists in the XML file
            NodeList nodeList = root.getElementsByTagName(field);
            if (nodeList.getLength() == 0) {
                System.out.println("Field '" + field + "' not found in XML file.");
                continue;
            }

            // Get the value of the first matching field
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                fieldValues.put(field, element.getTextContent());
            }
        }

        return fieldValues;
    }

}
