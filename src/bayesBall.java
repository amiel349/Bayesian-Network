import java.util.LinkedList;
import java.util.Queue;

public class bayesBall {
    private myNode start, end;
    private LinkedList<myNode> observed;
    private LinkedList<myNode> evidence;
    private bayesianNetwork bn;
    private String line;
    public bayesBall(bayesianNetwork bn,String line){
        this.bn=bn;
        this.line=line;
        observed=new LinkedList<>();
        getStartAndEnd();
        setEvidence();
    }
    private void getStartAndEnd(){
        String first=line.substring(0,line.indexOf("-"));
        String second=line.substring(line.indexOf("-")+1,line.indexOf("|"));
        start=bn.findNode(first);
        end= bn.findNode(second);
    }
    private void setEvidence(){
        if(line.indexOf("|")==line.length()-1)
            return;
        String evidenceString=line.substring(line.indexOf("|")+1);
        String[] splittedEvidence;
        splittedEvidence=evidenceString.split(",");
            for (String key:splittedEvidence) {
                bn.findNode(key.substring(0,key.indexOf("="))).setColor(true);
            }return;

    }
    public String bayesBallQuery(){
        if(start.getChildrens().contains(end)){
            //System.out.println("first return ");
            return "no";}
        if(start.getParents().contains(end)){//System.out.println("second return ");
            return "no";}
        Queue<myNode> q = new LinkedList<>();

        for (myNode x: start.getChildrens()) {  //adding childs to q and set them from parent
            x.setFromParent(true);
            q.add(x);
        }
        for (myNode y: start.getParents()) { //adding parents to q and set them from children
            y.setFromChild(true);
            q.add(y);
        }
        while(!q.isEmpty()){
           myNode node=q.poll();
            //System.out.println(" now node is "+node.getName());
           if(!node.getColor()){
             if(node==end){//System.out.println("third return ");
                 return "no";}
             if(node.isFromChild()) {
                 //System.out.println("node is not colored and from child");
                 node.setFromChild(false);
                 if (node.getChildrens().contains(end)){//System.out.println("fourth return ");
                     return "no";}
                 for (myNode nodeChildren : node.getChildrens()) {
                    // System.out.println(nodeChildren.getName()+" is chldren of "+node.getName());
                     if (!nodeChildren.isVisitedByParent()) {
                         nodeChildren.setVisitedByParent(true);
                         nodeChildren.setFromParent(true);
                         q.add(nodeChildren);
                         //System.out.println(node.getName()+" is adding to q "+nodeChildren.getName());
                     }
                 }
                 if (node.getParents().contains(end)){//System.out.println("fifth return ");
                     return "no";}

                 for (myNode nodeParent : node.getParents()) {
                     if (!nodeParent.isVisitedByChild()) {
                         nodeParent.setVisitedByChild(true);
                         nodeParent.setFromChild(true);
                         q.add(nodeParent);
                        // System.out.println(node.getName()+" is adding to q "+nodeParent.getName());
                     }
                 }
             }
             else  // node is not from child (is from parent)
                 {   node.setFromParent(false);
                    // System.out.println("node is not colored and  from parent");
                     if(node.getChildrens().contains(end)){//System.out.println("sixth return ");
                         return "no";}
                     for (myNode nodeChildren: node.getChildrens()) {
                         if(!nodeChildren.isVisitedByParent()){
                             nodeChildren.setVisitedByParent(true);
                             nodeChildren.setFromParent(true);
                             q.add(nodeChildren);
                            // System.out.println(node.getName()+" is adding to q "+nodeChildren.getName());
                         }
                     }

             }
           }
           else // node is colored (observed)
           {
               if(node.isFromParent()){node.setFromParent(false);
                  // System.out.println("node is colored and from parent");
                   if(node.getParents().contains(end)){//System.out.println("seven return ");
                       return "no";}
                   for ( myNode nodeparent: node.getParents()) {
                       if(!nodeparent.isVisitedByChild()){
                           nodeparent.setVisitedByChild(true);
                           nodeparent.setFromChild(true);
                           q.add(nodeparent);
                          // System.out.println(node.getName()+" is adding to q "+nodeparent.getName());
                       }
                   }

               }

           }
        }

        return "yes";
    }

}
