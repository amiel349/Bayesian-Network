import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class myNode {
    private boolean FromParent;
    private boolean FromChild;
    private boolean color;
    private boolean visitedByParent;
    private boolean visitedByChild;
    private boolean visitedByBFS;
    private String name;
    private LinkedList<myNode> parents;
    private LinkedList<myNode> childrens;
    private HashMap<Integer,Double>CPT;
    private ArrayList<ArrayList<String>>table;
    private ArrayList<String>outcome;
    private int size;

    public myNode(String name){
        FromChild=false;
        FromParent=false;
        color=false;
        this.name=name;
        parents=new LinkedList<>();
        childrens=new LinkedList<>();
        CPT=new HashMap<>();
        size=2;
        table=new ArrayList<>();
        outcome=new ArrayList<>();
        visitedByBFS=false;


    }
    public boolean isVisitedByBFS() {
        return visitedByBFS;
    }

    public void setVisitedByBFS(boolean visitedByBFS) {
        this.visitedByBFS = visitedByBFS;
    }

    public String[] getOutcome() {
        //System.out.println(name+" asking"+outcome.toString());
        return outcome.toArray(new String[outcome.size()]);
    }
    public void addoutcome(String s){
        outcome.add(s);
    }

    public void tableAdd(String s){
       // System.out.println(name+" "+s+ " added to table");
        ArrayList<String>list=new ArrayList<>();
        list.add(s);
        table.add(list);

    }
    public ArrayList<String> getFromTable(String s){
        //System.out.println(s+" get from table");
        for (int i = 0; i <table.size() ; i++) {
            if(table.get(i).get(0)==s)
                return table.get(i);
        }

        return null;
    }

    public void setSize(int size){
        this.size+=size;
    }

    public int getSize() {
        return size;
    }

    public void addParent(myNode parent){
        this.parents.add(parent);
    }
    public void addChild(myNode child){
        this.childrens.add(child);
    }

    public LinkedList<myNode> getChildrens() {
        return childrens;
    }

    public LinkedList<myNode> getParents() {
        return parents;
    }
    public void addCPT(int index,double value){
        CPT.put(index,value);
    }

    public Collection<Double> getCPT() {
        return CPT.values();
    }

    public void setFromChild(boolean fromChild) {
        FromChild = fromChild;
    }

    public void setFromParent(boolean fromParent) {
        FromParent = fromParent;
    }
    public void setColor(boolean color){
        this.color=color;
    }
    public boolean getColor(){
        return color;
    }
    public String getName(){
        return name;
    }

    public boolean isFromParent() {
        return FromParent;
    }

    public boolean isFromChild() {
        return FromChild;
    }
    public boolean isVisitedByParent() {
        return visitedByParent;
    }
    public boolean isVisitedByChild() {
        return visitedByChild;
    }

    public void setVisitedByParent(boolean visitedByParent) {
        this.visitedByParent = visitedByParent;
    }

    public void setVisitedByChild(boolean visitedByChild) {
        this.visitedByChild = visitedByChild;
    }

    public ArrayList<ArrayList<String>> getTable() {
        return table;
    }

    public void tableToPrint(){
        System.out.println(name + " table");
        for (int i = 0; i <table.get(0).size(); i++) {
            for (int j = 0; j < table.size(); j++) {
                System.out.print(table.get(j).get(i) + " ");
            }
            System.out.println();
        }

    }
     public int getAsciiSize(){
        int sum=0;
         for (int i = 0; i <table.size()-1 ; i++) {
             sum+=Integer.valueOf(table.get(i).get(0));
         }
         return sum;
     }
//    @Override
//    public int compareTo(myNode o) {
//        if(this.getTable().get(0).size()>o.getTable().get(0).size())
//            return -1;
//        if(this.getTable().get(0).size()<o.getTable().get(0).size())
//            return 1;
//        if(this.getAsciiSize()>o.getAsciiSize())
//            return -1;
//        if(this.getAsciiSize()<o.getAsciiSize())
//            return 1;
//        return 0;
//    }
}
