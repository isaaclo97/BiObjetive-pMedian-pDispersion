package grafo.bpmd.algorithms;

import grafo.bpmd.structure.BPMDInstance;
import grafo.optilib.metaheuristics.Algorithm;
import grafo.optilib.results.Result;
import grafo.optilib.structure.Solution;
import grafo.optilib.tools.Timer;
import gurobi.*;

public class ModeloIsaac implements Algorithm<BPMDInstance> {

    private int timeLimit;

    public ModeloIsaac(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Result execute(BPMDInstance instance) {
        Result r = new Result(instance.getName());
        GRBEnv env = null;
        int n = instance.getN();
        int p = instance.getP();
        try {
            env = new GRBEnv("log_bpmd.txt");
            env.set(GRB.DoubleParam.TimeLimit, timeLimit);
            env.set(GRB.IntParam.LogToConsole, 0);
            // Starts writing nodes to disk when reaching XXX M
            //            env.set(GRB.DoubleParam.NodefileStart, 0.05);
            // Reduce the number of threads to reduce memory usage
            //            env.set(GRB.IntParam.Threads, 1);
            // Presolve 0 off 1 conservative 2 aggresive
            //            env.set(GRB.IntParam.Presolve, 0);
            GRBModel model = new GRBModel(env);

            // VARIABLES
            GRBVar[][] x = new GRBVar[n+1][n+1];
            GRBVar[] y = new GRBVar[n+1];
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    x[i][j] = model.addVar(0.0, 1.0, 0.0, GRB.INTEGER, "x["+i+"]["+j+"]");
                }
                y[i] = model.addVar(0.0, 1.0, 0.0, GRB.INTEGER, "y["+i+"]");
            }

            // OBJECTIVE FUNCTION
//            GRBVar z = model.addVar(0.0, GRB.INFINITY, 1.0, GRB.INTEGER, "z");
//            GRBLinExpr obj = new GRBLinExpr();
//            obj.addTerm(1.0, z);
//            model.setObjective(obj, GRB.MINIMIZE);

            GRBVar w = model.addVar(0.0, GRB.INFINITY, 1.0, GRB.INTEGER, "w");
            GRBLinExpr obj = new GRBLinExpr();
            obj.addTerm(1.0, w);
            model.setObjective(obj, GRB.MAXIMIZE);


            // CONSTRAINTS

//            for (int i = 1; i <= n; i++) {
//                GRBLinExpr r0 = new GRBLinExpr();
//                for (int j = 1; j <= n; j++) {
//                    int d = instance.getDist(i,j);
//                    r0.addTerm(d, x[i][j]);
//                }
//                model.addConstr(r0, GRB.LESS_EQUAL, z, "r0_"+i);
//            }

            for (int i = 1; i <= n; i++) {
                GRBLinExpr r1 = new GRBLinExpr();
                for (int j = 1; j <= n; j++) {
                    r1.addTerm(1, x[i][j]);
                }
                model.addConstr(r1, GRB.EQUAL, 1, "r1_"+i);
            }

            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    model.addConstr(x[i][j], GRB.LESS_EQUAL, y[j], "r3_"+i+"_"+j);
                }
            }

            GRBLinExpr r4 = new GRBLinExpr();
            for (int j = 1; j <= n; j++) {
                r4.addTerm(1.0, y[j]);
            }
            model.addConstr(r4, GRB.EQUAL, p, "r4");

            int M = 0;
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    M = Math.max((int)instance.getDist(i,j), M);
                }
            }

            for (int i = 1; i < n; i++) {
                for (int j = i+1; j <= n; j++) {
                    GRBLinExpr rightTerm = new GRBLinExpr();
                    rightTerm.addConstant(2);
                    rightTerm.addTerm(-1, y[i]);
                    rightTerm.addTerm(-1, y[j]);
                    GRBLinExpr r5 = new GRBLinExpr();
                    r5.multAdd(M, rightTerm);
                    r5.addConstant(instance.getDist(i,j));
                    model.addConstr(w, GRB.LESS_EQUAL, r5, "r5_"+i+"_"+j);
                }
            }

            model.update();
            Timer.initTimer();
            model.optimize();

            int status = model.get(GRB.IntAttr.Status);
            System.out.print(status+"\t");
            double secs = Timer.getTime()/1000.0;
            System.out.print(secs+"\t");
            if (status != GRB.INFEASIBLE) {
                double of = model.get(GRB.DoubleAttr.ObjVal);
                System.out.println(of);
                r.add("OF", of);
//                System.out.println("YVAR");
//                for (int j = 1; j <= n; j++) {
//                    System.out.println(j+" --> "+y[j].get(GRB.DoubleAttr.X));
//                }
//                for (GRBLinExpr constraint : constraints) {
//                    System.out.println("CONST:"+constraint.getValue());
//                }
                System.out.println("Selected: ");
                for (int j = 1; j <= n; j++) {
                    if (y[j].get(GRB.DoubleAttr.X) == 1) {
                        System.out.print("S["+j+"]: ");
                        for (int i = 1; i <= n; i++) {
                            if (i == j) continue;
                            if (x[i][j].get(GRB.DoubleAttr.X) == 1) {
                                System.out.print("["+i+","+instance.getDist(i,j)+"]");
                            }
                        }
                        System.out.println();
                    }
                }
            } else {
                System.out.println("INFEASIBLE");
                r.add("OF", -1);
            }
            r.add("Time (s)", secs);
            r.add("Status", status);

        } catch (GRBException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Solution getBestSolution() {
        return null;
    }
}
