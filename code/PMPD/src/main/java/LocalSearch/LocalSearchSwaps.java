package LocalSearch;

import grafo.optilib.metaheuristics.Improvement;
import grafo.optilib.tools.RandomManager;
import structure.PMPDSolution;
import structure.Pareto;

import java.util.ArrayList;
import java.util.Collections;

//SWAPS
public class LocalSearchSwaps implements Improvement<PMPDSolution> {

    public void improve(PMPDSolution solution) {
        RandomManager.getRandom();
        int iters = 3000;
        PMPDSolution newSolution = new PMPDSolution(solution);
        double comparativeDistance = solution.getInstance().getDistance()*solution.getInstance().getMaxValueToOneNode();
        boolean improveResult = true;
        ArrayList<Integer> nodes  = new ArrayList<>();
        ArrayList<Integer> nodesInSolution  = new ArrayList<>();
        for(int i = 1; i<newSolution.getInstance().getNodes();i++){
            if(newSolution.isInSolution(i)) nodesInSolution.add(i);
            else nodes.add(i);
        }
        long fin = System.currentTimeMillis();
        long tiempo = (long) ((fin - solution.getStartTime())/1000);
        while(improveResult && iters!=0 && tiempo<(solution.getTime())){
            Collections.shuffle(nodes);
            Collections.shuffle(nodesInSolution);
            improveResult = false;
            for(int i=0; i<nodesInSolution.size() && !improveResult && iters!=0 && tiempo<(solution.getTime());i++){
                for(int j=0; j<nodes.size()  && !improveResult && iters!=0 && tiempo<(solution.getTime());j++){
                    int remove = nodesInSolution.get(i);
                    int add = nodes.get(j);
                    fin = System.currentTimeMillis();
                    tiempo = (long) ((fin - solution.getStartTime())/1000);
                    //System.out.println(tiempo);
                    if(solution.getInstance().getGraph()[remove][add]>comparativeDistance)
                        continue;
                    swapMovement(newSolution, add, remove);
                    Pareto.add(newSolution);
                    iters--;
                    newSolution.checkFeasible();
                    if(Double.compare(solution.onlyDispersion(),newSolution.onlyDispersion())>0){
                        //Pareto.add(newSolution);
                        nodesInSolution.remove(i);
                        nodes.remove(j);
                        nodesInSolution.add(add);
                        nodes.add(remove);
                        solution.copy(newSolution);
                        //System.out.println("CurBest: " + solution.getMark());
                        improveResult = true;
                        newSolution.checkFeasible();
                        continue;
                    }
                    //DESHACER CAMBIO
                    swapMovement(newSolution, remove, add);
                    newSolution.checkFeasible();
                }
            }
        }
    }

    private void swapMovement(PMPDSolution newSolution, int add, int remove) {
        int size = newSolution.getS().size();
        newSolution.addToSolution(add);
        newSolution.removeToSolution(remove);
        int size2 = newSolution.getS().size();
        if(size!=size2){
            System.out.printf("BUG");
        }
    }
}
