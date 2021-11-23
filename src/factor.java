import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class factor implements Comparable<factor> {

    private ArrayList<ArrayList<String>>table;
    private String name;
    private HashMap<String,String>map;
    private String value;
    private myNode node;
    public factor(myNode node, HashMap<String,String> map){
        table=new ArrayList<>();
        this.node=node;
        this.map=map;
        name= node.getName();
    }
    public factor(String a,String b){
        table=new ArrayList<>();

        this.map=new HashMap<>();
        name=a+b;
    }
    public void createFactor( ){

        ArrayList<ArrayList<String>>copy= node.getTable();
        HashMap<Integer,String>valuesToRemove=new HashMap<>();
        for (int i = 0; i < copy.size() ; i++) {
            ArrayList<String>temp=new ArrayList<>();     // copy table from node
            for (String tableCopy:copy.get(i)) {
                  temp.add(tableCopy);
            }
            table.add(temp);
        }


        for (int i = 0; i <table.size()-1 ; i++) {
            if (map.containsKey(table.get(i).get(0))) {

                valuesToRemove.put(i, map.get(table.get(i).get(0)));
            }
        }

                    for (int k = 1; k <table.get(0).size() ; k++) {
                        for (Integer key : valuesToRemove.keySet()) {
                         if(!table.get(key).get(k).equals(valuesToRemove.get(key))){
                             for (int j = 0; j <table.size() ; j++) {
                                 table.get(j).remove(k);
                             }k--;
                             break;
                         }
                        }
                    }
            for (int j = 0; j <table.size() ; j++) {
                if(map.containsKey(table.get(j).get(0))){
                    table.remove(j);// removing from table the column and decrease index by one
                    j--;
                }
            }
    }



    public String getName() {
        return name;
    }
    public myNode getNode() {
        return node;
    }

    public ArrayList<ArrayList<String>> getTable() {
        return table;
    }

    public void setTable(ArrayList<ArrayList<String>> table) {
        this.table = table;
    }

    @Override
    public String toString() {

        System.out.println( this.getName()+ " factor");
        for (int i = 0; i <table.get(0).size(); i++) {
            for (int j = 0; j < table.size(); j++) {
                    System.out.print(table.get(j).get(i) + " ");
            }
            System.out.println();
        }
        return "";
    }
    public int getAscii(factor o1){
        int sum=0;
        for (int i = 0; i <o1.getTable().size()-1 ; i++) {
            sum+=o1.getTable().get(i).get(0).charAt(0)-'A';
        }
        return sum;
    }

    @Override
    public int compareTo(factor o) {
        if(this.getTable().size()>o.getTable().size())
            return 1;
        if(this.getTable().size()<o.getTable().size())
            return -1;
        if(getAscii(this)>getAscii(o))
            return 1;
        if(getAscii(this)<getAscii(o))
            return -1;
        return 0;
    }
}
