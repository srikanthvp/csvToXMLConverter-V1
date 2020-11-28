import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Utils {

    public static HashMap<String, ArrayList<String>> convertCSVToMap(final String filePath) throws IOException {

        HashMap<String, ArrayList<String>> map = new HashMap<>();
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            String fileDataString = new String(bytes);

            String[] linesArr = fileDataString.split("\n");
            String[] keyArr = linesArr[0].split(",");

            for (int i=1; i < linesArr.length; i++){
                String[] innerSplit = linesArr[i].split(",");

                for (int j=0; j < innerSplit.length; j++){
                    if (!map.containsKey(keyArr[j])){
                        map.put(keyArr[j], new ArrayList<>());
                        map.get(keyArr[j]).add(innerSplit[j]);
                    }else {
                        map.get(keyArr[j]).add(innerSplit[j]);
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return map;
    }

    public static void convertMapToXML(HashMap<String, ArrayList<String>> map, String xmlFilePath) throws ParserConfigurationException, IOException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
        FileWriter writer = null;

        if (!map.isEmpty()) {
            try {
                Document newDoc = domBuilder.newDocument();
                // Root element
                Element rootElement = newDoc.createElement("cmData");
                rootElement.setAttribute("type", "actual");
                newDoc.appendChild(rootElement);

                ArrayList<String> arrayList = map.get("Cbis");
                ArrayList<String> arrayList1 = map.get("fdn");
                ArrayList<String> arrayList2 = map.get("host-name");

                int index = 0;
                while (index < arrayList.size()) {
                    Element rowElement = newDoc.createElement("Cbis");
                    rowElement.setAttribute("id", arrayList.get(index));
                    rootElement.appendChild(rowElement);

                        if (map.containsKey("fdn")) {
                            Element curElement = newDoc.createElement("p");
                            curElement.appendChild(newDoc.createTextNode(arrayList1.get(index)));
                            curElement.setAttribute("name", "fdn");
                            rowElement.appendChild(curElement);
                        }
                        if (map.containsKey("host-name")) {
                            Element curElement = newDoc.createElement("p");
                            curElement.appendChild(newDoc.createTextNode(arrayList2.get(index)));
                            curElement.setAttribute("name", "host-name");
                            rowElement.appendChild(curElement);
                        }
                    ++index;
                }


                writer = new FileWriter(new File(xmlFilePath));
                TransformerFactory tranFactory = TransformerFactory.newInstance();
                Transformer aTransformer = tranFactory.newTransformer();
                aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
                aTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
                aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                Source src = new DOMSource(newDoc);
                Result result = new StreamResult(writer);
                aTransformer.transform(src, result);

                writer.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                writer.close();
            }
        }
        }

    }
