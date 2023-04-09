package structure;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Pareto {

    private static List<PMPDSolution> front;
    private static boolean modifiedSinceLastAsk;
    private static boolean globalModified;

    public synchronized static void reset() {
        front = new ArrayList<>(1000);
        modifiedSinceLastAsk = false;
        globalModified = false;
    }

    public synchronized static List<PMPDSolution> getFront() {
        return front;
    }

    public synchronized static boolean add(PMPDSolution newSol) {
        List<Integer> dominated = new ArrayList<>();
        boolean enter = true;
        int idx = 0;
        for (PMPDSolution frontSol : front) {
            double c0 = newSol.getOF(0) - frontSol.getOF(0);
            double c1 = newSol.getOF(1) - frontSol.getOF(1);
            if (c0 >= 0 && c1 <= 0) {
                enter = false;
                break;
            } else if (c0 <= 0 && c1 >= 0) {
                dominated.add(idx);
            }
            idx++;
        }
        int removed = 0;
        for (int idRem : dominated) {
            front.remove(idRem-removed);
            removed++;
        }
        if (enter) {
            front.add(new PMPDSolution(newSol));
            modifiedSinceLastAsk = true;
            globalModified = true;
        }
        return enter;
    }

    public static synchronized boolean isModifiedSinceLastAsk() {
        boolean ret = modifiedSinceLastAsk;
        modifiedSinceLastAsk = false;
        return ret;
    }

    public static synchronized boolean isGlobalModified() {
        boolean ret = globalModified;
        globalModified = false;
        return ret;
    }

    public static String toText() {
        StringBuilder stb = new StringBuilder();
        stb.append("pMedian").append("\t").append("pDispersion").append("\n");
        for (PMPDSolution sol : front) {
           stb.append(sol.getOF(0)).append("\t").append(sol.getOF(1)).append("\n");
        }
        return stb.toString();
    }

    public static void saveToFile(String path) {
        File folder = new File(path);
        try {
            PrintWriter pw = new PrintWriter(folder);
            pw.print(toText());
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
