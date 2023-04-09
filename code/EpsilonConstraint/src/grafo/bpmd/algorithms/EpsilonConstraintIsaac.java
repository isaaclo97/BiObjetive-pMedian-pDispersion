package grafo.bpmd.algorithms;

import grafo.bpmd.structure.BPMDInstance;
import grafo.optilib.metaheuristics.Algorithm;
import grafo.optilib.results.Result;
import grafo.optilib.structure.Solution;
import grafo.optilib.tools.Timer;
import gurobi.*;

import java.util.ArrayList;
import java.util.List;

public class EpsilonConstraintIsaac implements Algorithm<BPMDInstance> {

    private int timeLimit;

    public EpsilonConstraintIsaac(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Result execute(BPMDInstance instance) {
        int n = instance.getN();
        int p = instance.getP();
        double M = 0;
        double epsMin = Integer.MAX_VALUE;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i == j) continue;
                M = Math.max(instance.getDist(i, j), M);
                epsMin = Math.min(instance.getDist(i,j), epsMin);
            }
        }

        double epsMax = (int) PDispersionSolver.solve(instance, timeLimit);
//        int eps = 0;
        int totalSteps = 32; //Promedio de puntos en el PMED
        //int totalSteps = 38; //Promedio de puntos en el CHEMA
        //int add = Math.max(1,(int)(epsMax-epsMin)/totalSteps);
        double add = (epsMax-epsMin)/totalSteps;
        Result r = new Result(instance.getName());
        for (double eps = epsMin; eps <= epsMax; eps+=add) {
//            System.out.print("EPS="+eps+"\t");
            GRBEnv env;
            try {
                env = new GRBEnv("log_bpmd.txt");
                env.set(GRB.DoubleParam.TimeLimit, timeLimit/totalSteps);
                env.set(GRB.IntParam.LogToConsole, 0);
                // Starts writing nodes to disk when reaching XXX M
                //            env.set(GRB.DoubleParam.NodefileStart, 0.05);
                // Reduce the number of threads to reduce memory usage
                //            env.set(GRB.IntParam.Threads, 1);
                // Presolve 0 off 1 conservative 2 aggresive
                //            env.set(GRB.IntParam.Presolve, 0);
                GRBModel model = new GRBModel(env);

                // VARIABLES de 1 a N, instancias igual
                GRBVar[][] x = new GRBVar[n+1][n+1];
                GRBVar[] y = new GRBVar[n+1];
                for (int i = 1; i <= n; i++) {
                    for (int j = 1; j <= n; j++) {
                        x[i][j] = model.addVar(0.0, 1.0, 0.0, GRB.INTEGER, "x["+i+"]["+j+"]");
                    }
                    y[i] = model.addVar(0.0, 1.0, 0.0, GRB.INTEGER, "y["+i+"]");
                }
                GRBVar w = model.addVar(0.0, GRB.INFINITY, 1.0, GRB.CONTINUOUS, "w");

//                 OBJECTIVE FUNCTION
                GRBLinExpr z = new GRBLinExpr();
                for (int i = 1; i <= n; i++) {
                    for (int j = 1; j <= n; j++) {
                        int d = (int)instance.getDist(i,j);
                        z.addTerm(d, x[i][j]);
                    }
                }
                model.setObjective(z, GRB.MINIMIZE);

                // CONSTRAINTS

                for (int i = 1; i <= n; i++) {
                    GRBLinExpr r4 = new GRBLinExpr();
                    for (int j = 1; j <= n; j++) {
                        r4.addTerm(1, x[i][j]);
                    }
                    model.addConstr(r4, GRB.EQUAL, 1, "r4_"+i);
                }

                for (int i = 1; i <= n; i++) {
                    for (int j = 1; j <= n; j++) {
                        model.addConstr(x[i][j], GRB.LESS_EQUAL, y[j], "r5_"+i + "_" + j);
                    }
                }

                GRBLinExpr r6 = new GRBLinExpr();
                for (int j = 1; j <= n; j++) {
                    r6.addTerm(1, y[j]);
                }
                model.addConstr(r6, GRB.EQUAL, p, "r6");


                for (int i = 1; i <= n; i++) {
                    for (int j = i+1; j <= n; j++) {
                        GRBLinExpr r7_M = new GRBLinExpr();
                        int d = (int) instance.getDist(i,j);
                        r7_M.addTerm(-1,y[i]);
                        r7_M.addTerm(-1,y[j]);
                        r7_M.addConstant(2);
                        GRBLinExpr r7 = new GRBLinExpr();
                        r7.multAdd(M,r7_M);
                        r7.addConstant(d);
                        model.addConstr(w, GRB.LESS_EQUAL, r7, "r7_"+i + "_" + j);
                    }
                }

                model.addConstr(w, GRB.GREATER_EQUAL, eps, "eps");

                model.update();
                Timer.initTimer();
                model.optimize();

                int status = model.get(GRB.IntAttr.Status);
                System.out.print(status + "\t");
                double secs = Timer.getTime() / 1000.0;
                System.out.print(secs + "\t");
                if (status != GRB.INFEASIBLE) {
                    double of = -1;
                    double f2 = -1;
                    try {
                        of = model.get(GRB.DoubleAttr.ObjVal);
                        f2 = w.get(GRB.DoubleAttr.X);
                    }catch (Exception e){
                        System.out.println("No tiene solucion");
                        continue;
                    }
//                  System.out.println("F1=" + of + "\tF2=" + f2);
                    System.out.println(of + "\t" + f2);
                    r.add("f1", of);
                    r.add("f2", f2);
                } else {
                    r.add("f1", -1);
                    r.add("f2", -1);
                }
                r.add("Time (s)", secs);
                r.add("Status", status);

            } catch (GRBException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Solution getBestSolution() {
        return null;
    }
}
