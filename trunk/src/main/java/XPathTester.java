import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;


public class XPathTester {

    public static void main(String[] args) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(args[0]));
            List nodes = document.selectNodes(args[1]);
            for (Object o : nodes) {
                if (o instanceof Node) {
                    Node n = (Node)o;
                    System.out.println("Node: " + n.getText() + " - " + n.getPath());
                } else if (o instanceof String) {
                    System.out.println("String: " + o);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
