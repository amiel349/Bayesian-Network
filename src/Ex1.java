import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class Ex1 {


    public static void main(String[] args) throws FileNotFoundException {
        FileParser f = new FileParser();
        LinkedList<Integer> list = new LinkedList<>();
        f.querydivider(list);
        FileParser ff = new FileParser();
        LinkedList<String> querylist = new LinkedList<>();
        ff.querySplitter(querylist);

//        bayesianNetwork bn = new bayesianNetwork(f.getXmlName());
//        bn.networkBuild();
//        bayesBall bb=new bayesBall(bn,"A1-D1|C3=T,B2=F,B3=F");
//        System.out.println(bb.bayesBallQuery());
       //VE ve = new VE(bn);
      //System.out.println(ve.calc("P(A2=T|C2=v1) D1-C1-B0-A1-B1-A3-C3-B2-B3"));
        int index = 0;
        try {

            FileWriter fWriter = new FileWriter(
                    "output.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fWriter);
            for (Integer key : list) {
                bayesianNetwork bn = new bayesianNetwork(f.getXmlName());
                bn.networkBuild();
                if (key == 0) {
                    bayesBall bb = new bayesBall(bn, querylist.get(index++));
                    //System.out.println();
                    bufferedWriter.write(bb.bayesBallQuery());
                    bufferedWriter.newLine();
                } else {
                    VE ve = new VE(bn);
                    //System.out.println(ve.calc(querylist.get(index++)));
                    bufferedWriter.write(ve.calc(querylist.get(index++)));
                    bufferedWriter.newLine();
                }
            }
            bufferedWriter.close();
        }
        catch (IOException e) {
            System.out.print(e.getMessage());
        }
   }
}

