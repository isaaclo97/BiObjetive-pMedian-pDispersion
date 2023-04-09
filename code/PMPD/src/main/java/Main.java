import LocalSearch.LocalSearchSwaps;
import LocalSearch.PathRelinking;
import algorithms.RPR;
import constructives.GreedyConstructive;
import grafo.optilib.metaheuristics.Algorithm;
import grafo.optilib.results.Experiment;
import structure.*;

import java.io.File;
import java.util.Calendar;

public class Main {
    public static void main(String[] args){

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String date = String.format("%04d-%02d-%02d T%02d-%02d", year, month, day, hour, minute);

        PMPDInstanceFactory factory = new PMPDInstanceFactory();
        String dir = ((args.length == 0) ? "../../instances/kmedian" : (args[0])); //../.  ./ instancias_nuevas
        String outDir = "experiments/" + date;
        File outDirCreator = new File(outDir);
        outDirCreator.mkdirs();
        String[] extensions = new String[]{".txt"};
        int iterations = 100;
        if(args.length>=2)
            iterations = Integer.parseInt(args[1]);
        double k = 0.75;
        if(args.length>=3)
            k = Double.parseDouble(args[2]);
        Algorithm<PMPDInstance>[] execution = new Algorithm[]{
                new RPR(new GreedyConstructive(),iterations,1,  new LocalSearchSwaps(), new PathRelinking(),k,"/RPR"),
        };

        for (int i = 0; i < execution.length; i++) {
            String outputFile = outDir + "/" + execution[i].toString() + "_" + i + ".xlsx";
            Experiment<PMPDInstance, PMPDInstanceFactory> experiment = new Experiment<>(execution[i], factory);
            experiment.launch(dir, outputFile, extensions);

        }
    }
}