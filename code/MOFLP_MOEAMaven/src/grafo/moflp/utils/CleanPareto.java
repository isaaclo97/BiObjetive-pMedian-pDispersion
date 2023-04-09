package moflp.utils;

import java.io.*;

public class CleanPareto {

    public static void main(String[] args) throws IOException {
        String pathIn = "./moea_pareto";
        String pathOut = "./moea_pareto_clean";
        new File(pathOut).mkdirs();
        String[] files = new File(pathIn).list((dir, name) -> name.endsWith(".txt"));
        for (String file : files) {
            BufferedReader bf = new BufferedReader(new FileReader(pathIn+"/"+file));
            bf.readLine();
            String line = null;
            PrintWriter pw = new PrintWriter(pathOut+"/"+file);
            while ((line = bf.readLine()) != null) {
                pw.println(line.replace(";","\t"));
            }
            pw.close();
            bf.close();
        }
    }
}
