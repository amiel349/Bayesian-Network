import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class FileParser {
   private File f;
   private Scanner sc;
   private String xmlName;

   public FileParser() throws FileNotFoundException {
       f=new File("src/input.txt");
       sc=new Scanner(f);
    }

    public LinkedList<Integer> querydivider(LinkedList<Integer> list){// divide querys to bayesball or eliminate variable
       String line;
        xmlName=sc.nextLine();
       while(sc.hasNext()){
           line=sc.nextLine();
           if(line.charAt(0)=='P'&&line.charAt(1)=='(')
               list.add(1);
           else
               list.add(0);
       }
       return list;
    }
    public LinkedList<String> querySplitter(LinkedList<String>list){
        xmlName=sc.nextLine();
       while (sc.hasNext())
           list.add(sc.nextLine().toString());
       return list;
    }
    public String getXmlName(){
       return xmlName;
    }
}
