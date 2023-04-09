package constructives;

import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.tools.RandomManager;
import structure.PMPDInstance;
import structure.PMPDSolution;
import structure.Pareto;


public class GreedyConstructive implements Constructive<PMPDInstance, PMPDSolution> {
    public PMPDSolution constructSolution(PMPDInstance instance) {
        PMPDSolution sol = new PMPDSolution(instance);
        sol.setAlpha(instance.getAlpha());
        for(int i=0; i<instance.getFacilities();i++){
            double curValue = 0x3f3f3f3f;
            int selectedNode = -1;
            for(int j=0; j<instance.getNodes();j++){
                if(sol.isInSolution(j)) continue;
                if(i+1 == instance.getFacilities()) {
                    sol.addToSolution(j);
                    Pareto.add(sol);
                    sol.removeToSolution(j);
                }
                int num = RandomManager.getRandom().nextInt(3);
                if(num==0) {
                    double res = sol.evaluate(j);
                    if (Double.compare(curValue, res) > 0) {
                        curValue = res;
                        selectedNode = j;
                    }
                }
            }
            sol.addToSolution(selectedNode);
        }
        sol.checkFeasible();
        return sol;
    }
}
