import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.*;

public class bayesianNetwork {
    private HashMap<String,myNode>map;
    private String xmlname;
    bayesianNetwork(String name){
        map=new HashMap<>();
        xmlname=name;
    }

    public void networkBuild(){
        try {
            File file = new File("src/"+xmlname);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);

            doc.getDocumentElement().normalize();

            NodeList var = doc.getElementsByTagName("VARIABLE");

            for (int i = 0; i < var.getLength(); ++i) { // filing map with variabels name
                Node node = var.item(i);
                    Element element = (Element)node;
                    myNode vertex=new myNode(element.getElementsByTagName("NAME").item(0).getTextContent());
                    createOutcome(node,vertex);
                    map.put(vertex.getName(),vertex);

            }

            NodeList def
                    = doc.getElementsByTagName("DEFINITION");

            for (int i = 0; i < def.getLength(); ++i) {
                Node node = def.item(i);
                NodeList neighbors= ((Element) node).getElementsByTagName("GIVEN");
                myNode child=map.get(((Element) node).getElementsByTagName("FOR").item(0).getTextContent());
               // child.tableAdd(0,0, child.getName());
                child.setSize(neighbors.getLength());
                String s=((Element) node).getElementsByTagName("TABLE").item(0).getTextContent();
                if(neighbors.getLength()>0)
                for (int j = 0; j <neighbors.getLength() ; j++) {
                    myNode parent=map.get(neighbors.item(j).getTextContent());
                    //child.tableAdd(j+1,0,parent.getName());
                    //child.setAsciiSize(parent.getName().charAt(0));
                    child.addParent(parent);
                    parent.addChild(child);
                }
                createTable(child,s);

            }

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    private void createOutcome(Node node, myNode vertex){
        NodeList outcomeList= ((Element) node).getElementsByTagName("OUTCOME");
        for (int i = 0; i <outcomeList.getLength() ; i++) {
            vertex.addoutcome(outcomeList.item(i).getTextContent());
        }
        vertex.setSize(outcomeList.getLength());
    }

    private void createTable(myNode node,String s){
       String[] splitValuesArr=s.split(" ");
       int size= splitValuesArr.length;
       int length=node.getParents().size();
       node.tableAdd( node.getName());
        for (myNode parent: node.getParents()) {
            node.tableAdd(parent.getName());
        }
        //System.out.println(node.getName()+" start debug");
       // node.tableToPrint();
        node.tableAdd("values");
        ArrayList<String>valuesList=node.getFromTable("values");
        for (String value: splitValuesArr) {  //inserting values to table
            valuesList.add(value);
        }
        int moduleIndex=0;
        String [] nodeOutcome=node.getOutcome();
        valuesList=node.getFromTable(node.getName());

        for (int i = 0; i <size ; i++) {        //inserting outcome for the node of the table
            valuesList.add(nodeOutcome[moduleIndex]);
            moduleIndex=(moduleIndex+1)% nodeOutcome.length;
        }

        int sumForEveryVar=node.getOutcome().length;
        //System.out.println(node.getName()+ " "+length);
        LinkedList<myNode>parents= node.getParents();
        Iterator<myNode> it= parents.descendingIterator();
        while (it.hasNext()){
            myNode currTableNode= it.next();
            valuesList= node.getFromTable(currTableNode.getName());
            //System.out.println(valuesList.toString());
            //System.out.println(currTableNode.getName());
            String [] currTableNodeOutcome=currTableNode.getOutcome();
           // System.out.println(Arrays.toString(currTableNodeOutcome));
            moduleIndex=0;
            for (int j = 0; j <size ; j+=sumForEveryVar) {
                for (int k = 0; k <sumForEveryVar ; k++) {
                    valuesList.add(currTableNodeOutcome[moduleIndex]);
                }
               // moduleIndex=(moduleIndex+1)% nodeOutcome.length;
                moduleIndex=(moduleIndex+1)% currTableNodeOutcome.length;
            }
            sumForEveryVar=sumForEveryVar* currTableNodeOutcome.length;

        }
    }
    public Set<String>getnodes(){
        return map.keySet();
    }
    public myNode findNode(String s){
        if(map.containsKey(s))
            return map.get(s);
        return null;
    }
    public Collection<myNode>getMap(){
        return map.values();
    }

    @Override
    public String toString() {
        Collection<String> col=map.keySet();
        for (String key:col) {
            System.out.print("node: "+key+" parents :");
            myNode ver=map.get(key);
            LinkedList<myNode>list=map.get(key).getParents();
            for (myNode parent :list) {
                System.out.print(parent.getName());
            }
            System.out.print("  CPT:");
            for (Double value:ver.getCPT()) {
                System.out.print(value+" ");
            }
            System.out.println();

        }
        return "";
    }
}

