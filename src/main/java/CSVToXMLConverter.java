import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CSVToXMLConverter {
    public static void main(String[] args) throws IOException, ParserConfigurationException {
        HashMap<String, ArrayList<String>> map;

        map = Utils.convertCSVToMap("src/main/resources/input.csv");
        Utils.convertMapToXML(map, "src/main/resources/output.xml");
    }
}
