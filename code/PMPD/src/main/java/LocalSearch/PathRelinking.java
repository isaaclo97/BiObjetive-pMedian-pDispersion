package LocalSearch;

import grafo.optilib.metaheuristics.Improvement;
import grafo.optilib.tools.RandomManager;
import structure.PMPDSolution;
import structure.Pareto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class PathRelinking implements Improvement<PMPDSolution> {

    public void improve(PMPDSolution solution) {
        boolean improveResult = true;
        PMPDSolution bestSol = null;
        RandomManager.getRandom();
        HashSet<PMPDSolution> verify = new HashSet<>();
        long fin = System.currentTimeMillis();
        long tiempo = (long) ((fin - solution.getStartTime())/1000);
        while(improveResult && tiempo<solution.getTime()){
            List<PMPDSolution> pareto = new ArrayList<>();
            for(PMPDSolution i: Pareto.getFront())
                pareto.add(new PMPDSolution(i));
            Collections.shuffle(pareto);
            improveResult = false;
            for(int i=0; i<pareto.size() && !improveResult && tiempo<solution.getTime();i++){
                for(int j=i+1; j<pareto.size()  && !improveResult && tiempo<solution.getTime();j++){
                    fin = System.currentTimeMillis();
                    tiempo = (long) ((fin - solution.getStartTime())/1000);
                    if(verify.contains(pareto.get(i)) && verify.contains(pareto.get(j)))
                        continue;
                    boolean interior =  checkPathRelinkingInterior(pareto.get(i),pareto.get(j));
                    if(interior){
                        improveResult = interiorRandomPathRelinking(pareto.get(i),pareto.get(j));
                    }
                    else{
                        improveResult = exteriorPathRelinking(pareto.get(i),pareto.get(j));
                    }
                    verify.add(pareto.get(i));
                    verify.add(pareto.get(j));

                    if (bestSol == null || Double.compare(bestSol.evaluate(), pareto.get(i).evaluate()) > 0) {
                        bestSol = new PMPDSolution(pareto.get(i));
                    }
                }
            }
        }
        solution = bestSol;
    }

    private boolean checkPathRelinkingInterior(PMPDSolution first, PMPDSolution second) {
        ArrayList<Integer> intersection = new ArrayList<>(second.getS());
        ArrayList<Integer> firstSetWithoutSecond = new ArrayList<>(first.getS());
        intersection.removeAll(firstSetWithoutSecond);
        return (first.getS().size()*first.getInstance().getK())>(intersection.size());
    }

    private boolean interiorRandomPathRelinking(PMPDSolution first, PMPDSolution second) {
        ArrayList<Integer> intersection = new ArrayList<>(second.getS());
        ArrayList<Integer> firstSetWithoutSecond = new ArrayList<>(first.getS());
        intersection.removeAll(firstSetWithoutSecond);
        firstSetWithoutSecond.removeAll(second.getS());
        PMPDSolution bestSol = new PMPDSolution(first);
        while(firstSetWithoutSecond.size()>=1) {
            int randomIndex = (int) (RandomManager.getRandom().nextDouble()*firstSetWithoutSecond.size());
            int newRandomIndex = (int) (RandomManager.getRandom().nextDouble()*firstSetWithoutSecond.size());
            int intersectValue = intersection.get(randomIndex);
            int firstSetValue = firstSetWithoutSecond.get(newRandomIndex);
            first.removeToSolution(firstSetValue);
            first.addToSolution(intersectValue);
            Pareto.add(first);
            if (Double.compare(bestSol.evaluate(), first.evaluate()) > 0) {
                bestSol = new PMPDSolution(first);
            }
            if (Pareto.isGlobalModified()) {
                return true;
            }
            intersection.remove((Integer)intersectValue);
            firstSetWithoutSecond.remove((Integer)firstSetValue);
        }
        first  = bestSol;
        return false;
    }

    private boolean interiorGreedyPathRelinking(PMPDSolution first, PMPDSolution second) {
        HashSet<Integer> intersection = new HashSet<>(second.getS());
        HashSet<Integer> firstSetWithoutSecond = new HashSet<>(first.getS());
        intersection.removeAll(firstSetWithoutSecond);
        firstSetWithoutSecond.removeAll(second.getS());
        int removeS = -1, removeFirstSet = -1;
        while(firstSetWithoutSecond.size()>=1) {
            PMPDSolution bestSol = null;
            for (int firstSet : firstSetWithoutSecond) {
                for (int s : intersection) {
                    first.removeToSolution(firstSet);
                    first.addToSolution(s);
                    Pareto.add(first);
                    if (bestSol == null || Double.compare(bestSol.evaluate(), first.evaluate()) > 0) {
                        bestSol = new PMPDSolution(first);
                        removeS = s;
                        removeFirstSet = firstSet;
                    }
                    if (Pareto.isGlobalModified()) {
                        first = bestSol;
                        return true;
                    }
                    first.addToSolution(firstSet);
                    first.removeToSolution(s);
                }
            }
            firstSetWithoutSecond.remove(removeFirstSet);
            intersection.remove(removeS);
            first = new PMPDSolution(bestSol);
        }
        return false;
    }

    private boolean exteriorPathRelinking(PMPDSolution first, PMPDSolution second) {
        ArrayList<Integer> intersection = new ArrayList<>();
        for(Integer i: second.getS()){
            if(first.getS().contains(i))
                intersection.add(i);
        }
        PMPDSolution bestSol = new PMPDSolution(first);
        while(intersection.size()>=1) {
            int randomIndex = (int) (RandomManager.getRandom().nextDouble()*first.getInstance().getNodes());
            int intersectionRandom = (int) (RandomManager.getRandom().nextDouble()*intersection.size());
            int firstSetValue = intersection.get(intersectionRandom);
            while(second.getS().contains(randomIndex) || first.getS().contains(randomIndex)){
                randomIndex = (int) (RandomManager.getRandom().nextDouble()*first.getInstance().getNodes());
            }
            first.removeToSolution(firstSetValue);
            first.addToSolution(randomIndex);
            Pareto.add(first);
            if (Double.compare(bestSol.evaluate(), first.evaluate()) > 0) {
                bestSol = new PMPDSolution(first);
            }
            if (Pareto.isGlobalModified()) {
                return true;
            }
            intersection.remove((Integer)firstSetValue);
        }
        first  = bestSol;
        return false;
    }
}
