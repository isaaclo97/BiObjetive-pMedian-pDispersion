package grafo.metrics;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Pareto {

    private List<double[]> front;
    private boolean[] minimizing;

    public Pareto(boolean[] minimizing) {
        front = new ArrayList<>(1000);
        this.minimizing = minimizing;
    }

    public int compareDouble(double f1, double f2) {
        if (Math.abs(f1-f2) < 0.0001) {
            return 0;
        } else if (f1 < f2) {
            return -1;
        } else {
            return +1;
        }
    }

    public boolean add(double[] solution) {
        List<Integer> dominated = new ArrayList<>();
        boolean enter = true;
        int idx = 0;
        for (double[] frontSol : front) {
            boolean bestInAll = true;
            boolean worstInAll = true;
            for (int i = 0; i < frontSol.length; i++) {
                int comp = compareDouble(solution[i], frontSol[i]);
                if (comp < 0) {
                    worstInAll = false;
                } else if (comp > 0)  {
                    bestInAll = false;
                }
            }
            if (worstInAll) {
                enter = false;
                break;
            }
            if (bestInAll) {
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
            front.add(solution.clone());
        }

        return enter;
    }

    public String toText() {
        StringBuilder stb = new StringBuilder();
        front.sort(new Comparator<double[]>() {
            @Override
            public int compare(double[] o1, double[] o2) {
                return Double.compare(o1[0],o2[0]);
            }
        });
        for (double[] sol : front) {
            for (double obj : sol) {
                stb.append(obj).append("\t");
            }
            stb.replace(stb.length()-1, stb.length(), "\n");
        }
        return stb.toString();
    }

    public void saveToFile(String path) {
        if (path.lastIndexOf('/') > 0) {
            File folder = new File(path.substring(0, path.lastIndexOf('/')));
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
        try {
            PrintWriter pw = new PrintWriter(path);
            pw.print(toText());
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String path) {
        try (BufferedReader bf = new BufferedReader(new FileReader(path))) {
            String line;
            boolean flag = true;
            while ((line = bf.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                if(flag)
                {
                    flag = false;
                    continue;
                }
                String[] tokens = line.split(";");
                if(tokens.length==1){
                    tokens = line.split("\t");
                }
                double[] objs = new double[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    objs[i] = Double.parseDouble(tokens[i]);
                    if (!minimizing[i]) {
                        objs[i] *= -1; // Para que sea siempre minimizar
                    }
                }
                add(objs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
