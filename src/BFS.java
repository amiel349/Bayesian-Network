
import java.util.*;

public class BFS {

        public static void isAncentor(myNode start, bayesianNetwork bn, HashMap<myNode,Integer>map) {
            //System.out.println(" start bfs from node "+start.getName());
            Collection<myNode> allNodes = bn.getMap();
            for (myNode node : allNodes) {
               node.setVisitedByBFS(false);
            }
            LinkedList<myNode> queue = new LinkedList<>();
            queue.add(start);
              start.setVisitedByBFS(true);
            while (!queue.isEmpty()) {
                myNode currNode = queue.pop();
                for (myNode parent : currNode.getParents()) {
                    if (!parent.isVisitedByBFS()) {
                        parent.setVisitedByBFS(true);
                        queue.add(parent);
                    }
                }
                currNode.setVisitedByBFS(true);
            }
            for (myNode node : allNodes) {
                if(!node.isVisitedByBFS()){
                    if(map.containsKey(node)){
                        map.replace(node, map.get(node),map.get(node)+1);
                    }
                    else {
                        map.put(node,1);
                    }
                }
                node.setVisitedByBFS(false);
            }
           // System.out.println(" ending bfs from node "+start.getName());
        }


    }

