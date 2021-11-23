import java.util.*;

public class variableElimination {
    private ArrayList<factor> hidden;
    private bayesianNetwork bn;
    private ArrayList<String> hiddenOrder;
    private ArrayList<factor> factors;
    private  int numOfSum;
    private  int numOfProduct;
    String resultType;

    public variableElimination(bayesianNetwork bn) {
        this.bn = bn;
        hidden = new ArrayList<>();
        hiddenOrder = new ArrayList<>();
        factors = new ArrayList<>();
        numOfProduct=0;
        numOfSum=0;
        resultType="";

    }

    //    public void findHidden(Set<String>notHidden){
//        Set<String>allNodes=bn.getnodes();
//        for (String node:allNodes) {
//            if(!notHidden.contains(node))
//                hidden.add(bn.findNode(node));
//        }
//
//    }


    public String directAnswer(String query) {
        query = query.replaceAll("\\s", "");
        HashMap<String, String> map = new HashMap<>();
        String removeGarbage = query.substring(query.indexOf('(') + 1, query.indexOf(')'));
        resultType=query.substring(query.indexOf("=")+1,query.indexOf("|"));

        String hiddenString = "";
        if (query.length() - query.indexOf(')') + 1 > 0)
            hiddenString = query.substring(query.indexOf(')') + 1);
        String orderForHiiden = "";
        if (query.substring(query.indexOf(')')+1).length() > 0) {
            orderForHiiden = query.substring(query.indexOf(')')+1);
            createOrder(orderForHiiden);
        }
        StringBuilder evidenceReomveGarbage = new StringBuilder();
        for (int i = 0; i < removeGarbage.length(); i++) {
            if ((removeGarbage.charAt(i) >= 65 && removeGarbage.charAt(i) <= 90) ||
                    (removeGarbage.charAt(i) >= 48 && removeGarbage.charAt(i) <= 57))
                evidenceReomveGarbage.append(removeGarbage.charAt(i));
            else
                evidenceReomveGarbage.append(',');
        }
        evidenceReomveGarbage.delete(0,4);
        String[] cleanQ = evidenceReomveGarbage.toString().split(",");
        for (int i = 0; i < cleanQ.length - 1; i = i + 2) {
            map.put(cleanQ[i], cleanQ[i + 1]);
        }

        myNode node = bn.findNode(cleanQ[0]);
        for (myNode temp : bn.getMap()) {
            factor tryng = new factor(temp, map);
            tryng.createFactor();
            factors.add(tryng);
        }
        // eliminateOneValuedFactor(factors);
        setHidden(hiddenString);
        for (factor f : factors) {
            System.out.println(f);
        }

        for (factor f : hidden) {
            System.out.println(f);
        }
        factor a=factors.get(0);
        if (!hiddenOrder.isEmpty()){
            a=getFactortoJoin(hiddenOrder.get(0));
        hidden.remove(a);
        while (!hidden.isEmpty()){
            factor factortoJoin = getFactortoJoin(hiddenOrder.get(0));
            while (factortoJoin != null) {

                a = joinHidden(a, factortoJoin);
                numOfProduct+=a.getTable().get(0).size()-1;

                if (hidden.contains(factortoJoin))
                    hidden.remove(factortoJoin);
                else
                    factors.remove(factortoJoin);
                factortoJoin = getFactortoJoin(hiddenOrder.get(0));
                if (factortoJoin == null)
                    break;
            }
            factor factorAfterSum=sumAndEliminate(a,hiddenOrder.get(0));
            numOfSum+=factorAfterSum.getTable().get(0).size()-1;
            if(factors.contains(a))
                factors.remove(a);
            if(hidden.contains(a))
                hidden.remove(a);
            factors.add(factorAfterSum);
            hiddenOrder.remove(0);
            if(hiddenOrder.isEmpty())
                break;
            a=getFactortoJoin(hiddenOrder.get(0));
        }
            System.out.println("***********");
            for (factor f:factors) {
                System.out.println(f);
            }
        }
        Collections.sort(factors);
        a=factors.get(0);
        factors.remove(a);
        while (factors.size()>1){
            factor factortoJoin = getFactortoJoin(a.getTable().get(0).get(0));
            a=joinHidden(a,factortoJoin);
            factors.remove(factortoJoin);
        }
        sumAndEliminate(a,a.getTable().get(0).get(0));
        normalize(a);
        numOfSum++;
        String finalValue="";
        for (int i = 1; i <a.getTable().get(0).size() ; i++) {
            if(resultType.equals(a.getTable().get(0).get(i)))
                finalValue=a.getTable().get(1).get(i);
        }
        if(finalValue.length()>7)
            finalValue=finalValue.substring(0,7);
        finalValue+=","+String.valueOf(numOfSum)+","+String.valueOf(numOfProduct);
        System.out.println(a);
//        LinkedList<String> parentsList = new LinkedList<>();
//        for (myNode var : node.getParents()) {
//            parentsList.add(var.getName());
//        }
//        for (String parent : map.keySet()) {
//            if (parent.equals(node.getName()))
//                continue;
//            if (!parentsList.contains(parent)) {
//                return "false";
//            }
//        }
//        return findProbability(map, node);
        return finalValue;
    }







//    public String directAnswer(String query) {
//        query = query.replaceAll("\\s", "");
//        HashMap<String, String> map = new HashMap<>();
//        String removeGarbage = query.substring(query.indexOf('(') + 1, query.indexOf(')'));
//        resultType=query.substring(query.indexOf("=")+1,query.indexOf("|"));
//
//        String hiddenString = "";
//        if (query.length() - query.indexOf(')') + 1 > 0)
//            hiddenString = query.substring(query.indexOf(')') + 1);
//        String orderForHiiden = "";
//        if (query.substring(query.indexOf(')')+1).length() > 0) {
//            orderForHiiden = query.substring(query.indexOf(')')+1);
//            //orderForHiiden=orderForHiiden.replaceAll("\\s", "");
//           // orderForHiiden=orderForHiiden.replaceAll("-", "");
//            createOrder(orderForHiiden);
//        }
//        StringBuilder evidenceReomveGarbage = new StringBuilder();
//        for (int i = 0; i < removeGarbage.length(); i++) {
//            if ((removeGarbage.charAt(i) >= 65 && removeGarbage.charAt(i) <= 90) ||
//                    (removeGarbage.charAt(i) >= 48 && removeGarbage.charAt(i) <= 57))
//                evidenceReomveGarbage.append(removeGarbage.charAt(i));
//            else
//                evidenceReomveGarbage.append(',');
//        }
//        evidenceReomveGarbage.delete(0,4);
//        String[] cleanQ = evidenceReomveGarbage.toString().split(",");
//        for (int i = 0; i < cleanQ.length - 1; i = i + 2) {
//            map.put(cleanQ[i], cleanQ[i + 1]);
//        }
//
//        myNode node = bn.findNode(cleanQ[0]);
//        for (myNode temp : bn.getMap()) {
//            factor tryng = new factor(temp, map);
//            tryng.createFactor();
//            factors.add(tryng);
//        }
//       // eliminateOneValuedFactor(factors);
//        setHidden(hiddenString);
//        for (factor f : factors) {
//            System.out.println(f);
//        }
//
//        for (factor f : hidden) {
//            System.out.println(f);
//        }
//        factor a=getFactortoJoin(hiddenOrder.get(0));
//        hidden.remove(a);
//        int k=0;
//        while (!hidden.isEmpty()) {
//        //while(k++<4){
//            factor factortoJoin = getFactortoJoin(hiddenOrder.get(0));
//            while (factortoJoin != null) {
//
//                a = joinHidden(a, factortoJoin);
//                numOfProduct+=a.getTable().get(0).size()-1;
//
//                if (hidden.contains(factortoJoin))
//                    hidden.remove(factortoJoin);
//                else
//                    factors.remove(factortoJoin);
//                factortoJoin = getFactortoJoin(hiddenOrder.get(0));
//                if (factortoJoin == null)
//                    break;
//            }
//            factor factorAfterSum=sumAndEliminate(a,hiddenOrder.get(0));
//            numOfSum+=factorAfterSum.getTable().get(0).size()-1;
//            if(factors.contains(a))
//                factors.remove(a);
//            if(hidden.contains(a))
//                hidden.remove(a);
//            factors.add(factorAfterSum);
//            hiddenOrder.remove(0);
//            if(hiddenOrder.isEmpty()){
//                a=factorAfterSum;
//                break;}
//            a=getFactortoJoin(hiddenOrder.get(0));
//        }
//        a=joinHidden(factors.get(0),a);
//        normalize(a);
//        numOfSum++;
//        String finalValue="";
//        for (int i = 1; i <a.getTable().get(0).size() ; i++) {
//            if(resultType.equals(a.getTable().get(0).get(i)))
//                finalValue=a.getTable().get(1).get(i);
//        }
//        if(finalValue.length()>7)
//            finalValue=finalValue.substring(0,7);
//        finalValue+=","+String.valueOf(numOfSum)+","+String.valueOf(numOfProduct);
//        System.out.println(a);
////        LinkedList<String> parentsList = new LinkedList<>();
////        for (myNode var : node.getParents()) {
////            parentsList.add(var.getName());
////        }
////        for (String parent : map.keySet()) {
////            if (parent.equals(node.getName()))
////                continue;
////            if (!parentsList.contains(parent)) {
////                return "false";
////            }
////        }
////        return findProbability(map, node);
//        return finalValue;
//    }


    public void setHidden(String s) {
        if (s.length() < 1)
            return;
        String[] splitedhidden = s.split("-");
        for (String var : splitedhidden) {
            for (int i = 0; i < factors.size(); i++) {
                if (var.equals(factors.get(i).getName())) {
                    hidden.add(factors.get(i));
                    factors.remove(i);
                }
            }
        }
        //eliminateOneValuedFactor(hidden);
    }

    private String findProbability(HashMap<String, String> map, myNode node) {

        ArrayList<ArrayList<String>> table = node.getTable();
        int[] probabilitySum = new int[table.get(0).size()];

        for (int i = 0; i < table.size(); i++) {
            for (int j = 0; j < table.get(i).size(); j++) {
                if (table.get(i).get(j).equals(map.get(table.get(i).get(0))))
                    probabilitySum[j]++;
            }
        }
        double sum = 0;
        for (int i = 0; i < probabilitySum.length; i++) {
            if (probabilitySum[i] == map.size())
                sum += Double.valueOf(table.get(table.size() - 1).get(i));
        }
        return String.valueOf(sum);

    }
    private void normalize(factor a){
        String [] result=new String[2];
        String value="";
        double sum=0;
        int length=a.getTable().size()-1;
        for (int i = 1; i <a.getTable().get(0).size() ; i++) {
              sum+=Double.valueOf(a.getTable().get(length).get(i));
        }
        ArrayList<String>newValues=new ArrayList<>();
        newValues.add("values");
        for (int i = 1; i <a.getTable().get(0).size() ; i++) {
            double normalizedValue=Double.valueOf(a.getTable().get(length).get(i))/sum;
            newValues.add(String.valueOf(normalizedValue));
        }
        a.getTable().remove(length);
        a.getTable().add(newValues);
      //  System.out.println("noramlllll");
       // System.out.println(a);


    }
    private void createOrder(String query) {
       String[] splittedQuery=query.split("-");
        for (String s:splittedQuery) {
            hiddenOrder.add(s);
        }
    }

    public factor getFactortoJoin(String f) {
        List<factor> list = new ArrayList<>();
        for (factor factor : factors) {
            for (int i = 0; i < factor.getTable().size() - 1; i++) {// cheking if there is a node with the same values as f
                if(f.equals(factor.getTable().get(i).get(0)))
                    list.add(factor);
            }
        }
        for (factor factor : hidden) {
            for (int i = 0; i < factor.getTable().size() - 1; i++) {
                    if(f.equals(factor.getTable().get(i).get(0)))
                    list.add(factor);
            }


        }
        if(list.size()==0)
                return null;
        if(hidden.contains(list.get(0)))
            hidden.remove(list.get(0));
        else
            factors.remove(list.get(0));

        return list.get(0);
    }
    public factor joinHidden(factor a, factor b){
        System.out.println("factor to join");
        System.out.println(a);
        System.out.println(b);
        if(isNotContained(a,b)){
//            System.out.println("not containeeddd");
//            System.out.println(a);
//            System.out.println(b);
//            System.out.println("**********");
            return join(a,b);
            }
        factor newFactor=new factor(a.getName(),b.getName());
        ArrayList<String>indexes=new ArrayList<>();
        factor smaller=getsmaller(a,b);
        factor bigger=a;
        if(smaller==a)
            bigger=b;
//        for (int i = 0; i <a.getTable().size()-1 ; i++) {
//            ArrayList<String>temp=new ArrayList<>();    //inserting a values
//            indexes.add(a.getTable().get(i).get(0));
//            temp.add(a.getTable().get(i).get(0));
//            newFactor.getTable().add(temp);
//        }
//        System.out.println("new factor after a inserts");
//        System.out.println(newFactor);
        for (int i = 0; i <bigger.getTable().size()-1 ; i++) {
            if(!indexes.contains(bigger.getTable().get(i).get(0))){  // inserting values from b
            ArrayList<String>temp=new ArrayList<>();
            temp.add(bigger.getTable().get(i).get(0));
                newFactor.getTable().add(temp);}
        }
        ArrayList<String>temp=new ArrayList<>();
        temp.add("values");
        newFactor.getTable().add(temp);

        HashMap<String,Integer>indexesToCopy=new HashMap<>();
        creatable(bigger,smaller,newFactor);
        ArrayList<String>outcome=new ArrayList<>();

        for (int i = 0; i <smaller.getTable().size()-1 ; i++) {
            indexesToCopy.put(smaller.getTable().get(i).get(0),i);//taking indexes from smaller
            outcome.add(smaller.getTable().get(i).get(0));
        }
        for (int i = 0; i <bigger.getTable().size()-1 ; i++) {
            if(indexesToCopy.containsKey(bigger.getTable().get(i).get(0)))// changing the indexes to bigger
            indexesToCopy.put(bigger.getTable().get(i).get(0),i);
        }
        for (int i = 1; i <smaller.getTable().get(0).size(); i++) {
            HashMap<String,String>mergeValues=new HashMap<>();
            String prob=smaller.getTable().get(smaller.getTable().size()-1).get(i);
            for (int j = 0; j <smaller.getTable().size()-1 ; j++) {//taking a line from smaller;
                mergeValues.put(smaller.getTable().get(j).get(0),smaller.getTable().get(j).get(i));
            }
            for (int j = 1; j <bigger.getTable().get(0).size() ; j++) {
                int sum=0;
                for (int k = 0; k < mergeValues.size() ; k++) {
                    if(!bigger.getTable().get(indexesToCopy.get(outcome.get(k))).get(j).equals(
                            mergeValues.get(outcome.get(k))
                    ))
                        break;
                    sum++;
                }
                if(sum== outcome.size()){
                    String prob2=bigger.getTable().get(bigger.getTable().size()-1).get(j);
                    String result=String.valueOf(Double.valueOf(prob2)*Double.valueOf(prob));
                    newFactor.getTable().get(newFactor.getTable().size()-1).remove(j);
                    newFactor.getTable().get(newFactor.getTable().size()-1).add(j,result);
                    }

            }
        }
        System.out.println("new factor ");
        System.out.println(newFactor);
        return newFactor;
    }
    public factor getsmaller(factor a,factor b){
        if(a.getTable().size()>b.getTable().size())
            return b;
        return a;
    }
    public void creatable(factor a, factor b,factor newfactor) {
        if (a.getTable().get(0).size() >= b.getTable().get(0).size()) {
            for (int i = 0; i < newfactor.getTable().size(); i++) {
                for (int j = 1; j < a.getTable().get(i).size(); j++) {
                    newfactor.getTable().get(i).add(a.getTable().get(i).get(j));
                }

            }
        } else {
            for (int i = 0; i < newfactor.getTable().size() - 1; i++) {
                for (int j = 0; j < b.getTable().get(i).size(); j++) {
                    newfactor.getTable().get(i).add(b.getTable().get(i).get(j));
                }
            }

        }
    }
    public factor sumAndEliminate(factor a,String key){
        if(a.getTable().size()==2)
            return a;
        ArrayList<String>newValues=new ArrayList<>();
        for (int i = 0; i <a.getTable().size()-1 ; i++) {
            if(a.getTable().get(i).get(0).equals(key))       //adding the right values without the value which get erase
                continue;
            newValues.add(a.getTable().get(i).get(0));
        }
        factor b=new factor(a.getName(),"");
        int size=1;
        for (String s:newValues) {
            size=size*bn.findNode(s).getOutcome().length;
        }
        ArrayList<String>firstValues=new ArrayList<>();
        firstValues.add(newValues.get(0));
        String[] firstValueArr=bn.findNode(newValues.get(0)).getOutcome();
        int modulo=0;
        for (int i = 0; i <size ; i++) {
            firstValues.add(firstValueArr[modulo]);
            modulo=(modulo+1)% firstValueArr.length;
        }
        ArrayList<ArrayList<String>>newTable=new ArrayList<>();
        newTable.add(firstValues);
        newValues.remove(0);
        int sumForEveryVar=firstValueArr.length;
        while (!newValues.isEmpty()){
            myNode currTableNode= bn.findNode(newValues.get(0));
            newValues.remove(0);
            ArrayList<String>valuesList=new ArrayList<>();
            valuesList.add(currTableNode.getName());
            String [] currTableNodeOutcome=currTableNode.getOutcome();
            modulo=0;
            for (int j = 0; j <size ; j+=sumForEveryVar) {
                for (int k = 0; k <sumForEveryVar ; k++) {
                    valuesList.add(currTableNodeOutcome[modulo]);
                }
                modulo=(modulo+1)% currTableNodeOutcome.length;
            }
            newTable.add(valuesList);
            sumForEveryVar=sumForEveryVar* currTableNodeOutcome.length;

        }
        b.setTable(newTable);
        ArrayList<String>probabilty=new ArrayList<>();
        probabilty.add("values");
        double sum=0;
        String []trueAndFalse=new String[b.getTable().size()];
        String []valuesFromOldTable=new String[b.getTable().size()];
        for (int i = 1; i <b.getTable().get(0).size(); i++) {
            for (int j = 0; j <b.getTable().size() ; j++) {
                trueAndFalse[j]=b.getTable().get(j).get(i);
            }
            sum=0;

            for (int j = 1; j <a.getTable().get(0).size() ; j++) {
                int index=0;
                for (int k = 0; k < a.getTable().size()-1; k++) {
                    if(a.getTable().get(k).get(0).equals(key))
                        continue;
                    valuesFromOldTable[index++] = a.getTable().get(k).get(j);
                }
                if (Arrays.deepEquals(trueAndFalse, valuesFromOldTable)) {
                   // System.out.println(Arrays.toString(trueAndFalse));
                    //System.out.println(Arrays.toString(valuesFromOldTable));
                    //System.out.println(a.getTable().get(a.getTable().size() - 1).get(j));
                    sum =sum+ Double.valueOf(a.getTable().get(a.getTable().size() - 1).get(j));
                    //System.out.println("sum issss "+sum);
                }
            }
            //System.out.println("adding sum "+sum);
            probabilty.add(String.valueOf(sum));
        }
        newTable.add(probabilty);
//        System.out.println("table after sum and eliminate");
//        System.out.println(b);
//        System.out.println("***");
        return b;


    }
    public boolean isNotContained(factor a, factor b){
        ArrayList<String> aList=new ArrayList<>();
        ArrayList<String> bList=new ArrayList<>();
        for (int i = 0; i <a.getTable().size()-1 ; i++) {
            aList.add(a.getTable().get(i).get(0));
        }
        for (int i = 0; i <b.getTable().size()-1 ; i++) {
            bList.add(b.getTable().get(i).get(0));
        }
        boolean aFlag=false;
        boolean bFlag=false;
        for (String aKey:aList) {
            if(!bList.contains(aKey)){
                aFlag=true;
            break;}
        }
        for (String bKey:bList) {
            if(!aList.contains(bKey)){
                bFlag=true;
            break;}
        }
        return aFlag&&bFlag;
    }
    public factor join(factor a,factor b){
        factor newFactor=new factor(a.getName(),b.getName());
        ArrayList<String>doNotRepeat=new ArrayList<>();
        ArrayList<ArrayList<String>> table=new ArrayList<>();
        for (int i = 0; i <a.getTable().size()-1 ; i++) {
            ArrayList<String>temp=new ArrayList<>();
            temp.add(a.getTable().get(i).get(0));
            doNotRepeat.add(a.getTable().get(i).get(0));
            table.add(temp);
        }
        for (int i = 0; i <b.getTable().size()-1 ; i++) {
            if(doNotRepeat.contains(b.getTable().get(i).get(0)))
                continue;
            ArrayList<String>temp=new ArrayList<>();
            temp.add(b.getTable().get(i).get(0));
            table.add(temp);
        }
        ArrayList<String>values=new ArrayList<>();
        values.add("values");

        int tableSize=1;
        for (int i = 0; i <table.size() ; i++) {
            tableSize=tableSize*bn.findNode(table.get(i).get(0)).getOutcome().length;
        }
        for (int i = 0; i <tableSize ; i++) {
            values.add("0");
        }table.add(values);

        int sumForEveryVar=bn.findNode(table.get(0).get(0)).getOutcome().length;
        int modulu=0;
        String []outcome =bn.findNode(table.get(0).get(0)).getOutcome();
        for (int i = 0; i <tableSize ; i++) {        //inserting outcome for the node of the table
            table.get(0).add(outcome[modulu]);
            modulu=(modulu+1)%outcome.length;
        }

        for (int i = 1; i <table.size()-1 ; i++) {
            String []curroutcome =bn.findNode(table.get(1).get(0)).getOutcome();
            int moduleIndex=0;
            for (int j = 0; j <tableSize ; j+=sumForEveryVar) {
                for (int k = 0; k <sumForEveryVar ; k++) {
                    table.get(i).add(curroutcome[moduleIndex]);
                }
                moduleIndex=(moduleIndex+1)% curroutcome.length;
            }
            sumForEveryVar=sumForEveryVar* curroutcome.length;
        }
        ArrayList<Integer>indexesForA=new ArrayList<>();
        ArrayList<Integer>indexesForAInAtable=new ArrayList<>();
        for (int i = 0; i <a.getTable().size()-1 ; i++) {
            String var=a.getTable().get(i).get(0);
            for (int j = 0; j <table.size()-1 ; j++) {
                String tableVar=table.get(j).get(0);
                if(var.equals(tableVar)){
                    indexesForAInAtable.add(i);
                    indexesForA.add(j);
                    break;
                }
            }
        }

        for (int i = 1; i <a.getTable().get(0).size() ; i++) {
           ArrayList<String>valuesToFill=new ArrayList<>();
            for (int j = 0; j < indexesForAInAtable.size(); j++) {
                    valuesToFill.add(a.getTable().get(indexesForAInAtable.get(j)).get(i));
            }
            for (int m = 1; m <tableSize+1 ; m++) {
                int Asize = 0;
                for (int k = 0; k < indexesForA.size(); k++) {
                    if (valuesToFill.get(k).equals(table.get(indexesForA.get(k)).get(m)))
                    Asize++;
                    else
                        break;
                }
                if (Asize == indexesForA.size()){
                    table.get(table.size() - 1).remove(m);
                    table.get(table.size() - 1).add(m, a.getTable().get(a.getTable().size() - 1).get(i));}
            }
        }

        ArrayList<Integer>indexesForB=new ArrayList<>();
        for (int i = 0; i <b.getTable().size()-1 ; i++) {
            String var=b.getTable().get(i).get(0);
            for (int j = 0; j <table.size()-1 ; j++) {
                String tableVar=table.get(j).get(0);
                if(var.equals(tableVar)){
                    indexesForB.add(j);
                    break;
                }
            }
        }
        for (int i = 1; i <b.getTable().get(0).size() ; i++) {
            ArrayList<String> valuesToFill = new ArrayList<>();
            for (int j = 0; j < b.getTable().size() - 1; j++) {
                valuesToFill.add(b.getTable().get(j).get(i));
            }
            for (int m = 1; m < tableSize + 1; m++) {
                int Bsize = 0;
                for (int k = 0; k < indexesForB.size(); k++) {
                    if (valuesToFill.get(k).equals(table.get(indexesForB.get(k)).get(m)))
                        Bsize++;
                    else
                        break;

                }
                if (Bsize == indexesForB.size()){
                    double x = Double.valueOf(table.get(table.size() - 1).get(m));
                    double y = Double.valueOf(b.getTable().get(b.getTable().size() - 1).get(i));
                     table.get(table.size() - 1).remove(m);
                    table.get(table.size() - 1).add(m, String.valueOf(x * y));
                }
            }
        }
          newFactor.setTable(table);
        System.out.println("newfactor");
        System.out.println(newFactor);
        return newFactor ;
    }
//    public void creatable2(factor a, factor b,factor newfactor){
//        int sum=1;
//        for (int i = 0; i <newfactor.getTable().size()-1 ; i++) {
//         sum=sum*bn.findNode(newfactor.getTable().get(i).get(0)).getOutcome().length;
//        }
//        for (int i = 0; i <a.getTable().size()-1 ; i++) {
//            for (int j = 1; j <a.getTable().get(i).size() ; j++) {
//                newfactor.getTable().get(i).add(a.getTable().get(i).get(j));
//            }
//        }

    }

