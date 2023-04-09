package grafo.bpmd.tests;

import grafo.bpmd.algorithms.EpsilonConstraintIsaac;
import grafo.bpmd.structure.BPMDInstance;
import grafo.optilib.metaheuristics.Algorithm;

public class TestAlgorithm {

    public static void main(String[] args) {
        String names[] = {"1000-50.txt","1000-150.txt","1000-250.txt","2000-100.txt","2000-300.txt","2000-500.txt","3000-150.txt","3000-450.txt","3000-750.txt","4000-200.txt","4000-600.txt","4000-1000.txt","5000-250.txt","5000-750.txt","5000-1250.txt"};
        for(int i=0;i<15;i++) {
            String path = "../../instancias_nuevas/"+names[i]; //../.
            System.out.println(path);
            BPMDInstance instance = new BPMDInstance(path);
            Algorithm<BPMDInstance> gurobiIsaac = new EpsilonConstraintIsaac(3600);
            gurobiIsaac.execute(instance);
            System.out.println("--");
        }
        /*for(int i=39;i<=40;i++) {
            String path = "../../instancias/pmed"+i+".txt";
            System.out.println(path);
            BPMDInstance instance = new BPMDInstance(path);
            Algorithm<BPMDInstance> gurobiIsaac = new EpsilonConstraintIsaac(7200);
            gurobiIsaac.execute(instance);
            System.out.println("--");
        }*/
        /*for(int i=1;i<=10;i++) {
            if(i==4) continue;
            String path = "../../instanciasChema/D_250_"+i+".txt";
            System.out.println(path);
            BPMDInstance instance = new BPMDInstance(path);
            Algorithm<BPMDInstance> gurobiIsaac = new EpsilonConstraintIsaac(7200);
            gurobiIsaac.execute(instance);
            System.out.println("--");
        }
        for(int i=1;i<=10;i++) {
            //if(i!=4) continue;
            String path = "../../instanciasChema/D_350_"+i+".txt";
            System.out.println(path);
            BPMDInstance instance = new BPMDInstance(path);
            Algorithm<BPMDInstance> gurobiIsaac = new EpsilonConstraintIsaac(7200);
            gurobiIsaac.execute(instance);
            System.out.println("--");
        }*/
    }
}
