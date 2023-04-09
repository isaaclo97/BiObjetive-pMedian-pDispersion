package moflp.solve;

import moflp.structure.MOFLPProblem;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

import java.io.*;
import java.util.Calendar;

public class MOFLPSolver {

    public static void main(String[] args) throws IOException {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH)+1;
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        String date = String.format("%04d-%02d-%02d T%02d-%02d", year, month, day, hour, minute);

        String pathIn = "./"; //../.
        String instanceSet = (args.length>0)?args[0]:"../../instances/kmedian";
        String dir = ((args.length>0)?args[1]:pathIn)+instanceSet;
        String algorithm = "SPEA2";
        if(args.length>1)
            algorithm = args[2];
        String outDir = "./pareto-"+algorithm+"/";
        File outDirCreator = new File(outDir);
        outDirCreator.mkdirs();

        String[] fileNames = new File(dir).list((dir1, name) -> name.endsWith(".txt"));
        //Cambiar fichero de experimentos
        PrintWriter pw = new PrintWriter(algorithm+".csv");
        NondominatedPopulation result = null;
        for (String fileName : fileNames) {
            System.out.print(fileName+"\t");
            pw.print(fileName.replace(".txt","")+";");
            String path = dir+"/"+fileName;
            BufferedReader bf = new BufferedReader(new FileReader(path));
            String line = bf.readLine();
            line = bf.readLine(); //Comment if kmedian instances
            String[] l = line.split(" "); //Change to " " or \t depending the instances format
            int p = Integer.parseInt(l[2]); //Change according instances format
            //int p =
            bf.close();
            long timeIni = System.currentTimeMillis();
            if(algorithm.equals("NSGAII")) {
                 result = new Executor()
                        .withProblemClass(MOFLPProblem.class, path)
                        .withAlgorithm("NSGAII")
                        .withMaxTime(3600000)
                        .withMaxEvaluations(1000000)
                        .withProperty("populationSize", 100)
                        .run();
            }
            else if(algorithm.equals("MOEAD")) {
                 result = new Executor()
                        .withProblemClass(MOFLPProblem.class, path)
                        .withAlgorithm("MOEAD")
                        .withMaxTime(3600000)
                        .withMaxEvaluations(1000000) //
                        .withProperty("populationSize", 100) //10000
                        .run();
            }
            else if(algorithm.equals("SPEA2")) {
                 result = new Executor()
                        .withProblemClass(MOFLPProblem.class, path)
                        .withAlgorithm("SPEA2")
                        .withMaxTime(3600000) //3600000
                        .withMaxEvaluations(1000000)
                        .withProperty("populationSize", 100)
                        .run();
            }
            else if(algorithm.equals("AbYSS")) {
                 result = new Executor()
                        .withProblemClass(MOFLPProblem.class, path)
                        .withAlgorithm("AbYSS")
                        .withMaxEvaluations(1000000)
                        .withProperty("populationSize", 100)
                        .run();
            }
            else if(algorithm.equals("OMOPSO")) {
                 result = new Executor()
                        .withProblemClass(MOFLPProblem.class, path)
                        .withAlgorithm("OMOPSO")
                        .withMaxEvaluations(1000000)
                        .withProperty("populationSize", 100)
                        .run();
            }
            double secs = (System.currentTimeMillis()-timeIni)/1000.0;

            PrintWriter pwPareto = new PrintWriter(outDir+"/"+fileName);
            pwPareto.println("pMedian;pDispersion");
            for (int i = 0; i < result.size(); i++) {
                Solution sol = result.get(i);
                double[] obj = sol.getObjectives();
                System.out.print("Solution "+(i+1)+": "+obj[0]+", "+(-obj[1])+" -> ");
                pwPareto.println(obj[0]+";"+(-obj[1]));
                for (int j = 0; j < p; j++) {
                    int fac = EncodingUtils.getInt(sol.getVariable(j));
                    System.out.print(fac  + " ");
                }
                System.out.println();
            }
            pw.println(secs);
            System.out.println(result.size()+"\t"+secs);
            pwPareto.close();
        }
        pw.close();
    }
}
