package grafo.bpmd.structure;

import grafo.optilib.structure.Instance;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class BPMDInstance implements Instance {

    private int n;
    private int p;
    private String name;
    private double[][] dist;

    public BPMDInstance(String path) {
        /*if(path.contains("pmed"))
            readInstance(path);
        else
            readInstanceChema(path);
        */
        //readInstance(path);
        readInstanceKMedian(path);
    }

    public void readInstanceKMedian(String path) {
        this.name = path.substring(path.lastIndexOf('\\') + 1);
        FileReader fr= null;
        try {
            fr = new FileReader(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br=new BufferedReader(fr);
        // read line by line
        String line;
        try{
            line = br.readLine();
            line = br.readLine();
            String[] l = line.split(" ");
            n = Integer.parseInt(l[0]);
            p = Integer.parseInt(l[2]);
            dist = new double[n+1][n+1];
            for(int i=0; i<n;i++){
                Arrays.fill(dist[i],0x3f3f3f3f);
            }
            for(int i=0; i<n;i++){
                line = br.readLine();
                l = line.split(" ");
                for(int j=0; j<l.length-2;j++){
                    int start = i + 1;
                    int end = j + 1;
                    int cost = Integer.parseInt(l[j+2]);
                    dist[start][end]=cost;
                    dist[end][start]=cost;
                }
            }
            br.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        FloydWarshall();
    }

    @Override
    public void readInstance(String path) {
        try {
            name = path.substring(path.lastIndexOf('/')+1, path.lastIndexOf('.'));
            BufferedReader bf = new BufferedReader(new FileReader(path));
            String line = bf.readLine().trim();
            String[] tokens = line.split(" ");
            n = Integer.parseInt(tokens[0]);
            dist = new double[n+1][n+1];
            for(int i=0; i<n;i++){
                Arrays.fill(dist[i],0x3f3f3f3f);
            }
            int m = Integer.parseInt(tokens[1]);
            p = Integer.parseInt(tokens[2]);
            for (int i = 0; i < m; i++) {
                tokens = bf.readLine().trim().split(" ");
                int v1 = Integer.parseInt(tokens[0]);
                int v2 = Integer.parseInt(tokens[1]);
                int d = Integer.parseInt(tokens[2]);
                dist[v1][v2] = d;
                dist[v2][v1] = d;
            }
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FloydWarshall();
    }

    public void readInstanceChema(String path) {
        this.name = path.substring(path.lastIndexOf('\\') + 1);
        System.out.println("Reading instance " + this.name);
        FileReader fr= null;
        try {
            fr = new FileReader(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br=new BufferedReader(fr);

        // read line by line
        String line;
        try{
            line = br.readLine();
            String[] l = line.split("\t");
            n = Integer.parseInt(l[0]);
            int edges = n*n;
            p = Integer.parseInt(l[1]);
            dist = new double[n+1][n+1];
            for(int i=0; i<n;i++){
                Arrays.fill(dist[i],0x3f3f3f3f);
            }
            while ((line = br.readLine()) != null) {
                l = line.split("\t");
                int start = Integer.parseInt(l[0]);
                int end = Integer.parseInt(l[1]);
                double cost = Double.parseDouble(l[2]);
                dist[start][end]=cost;
                dist[end][start]=cost;
            }
            br.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void FloydWarshall(){
        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    dist[i][j]=Math.min(dist[i][j],dist[i][k]+dist[k][j]);
    }

    public double getDist(int v1, int v2) {
        return dist[v1][v2];
    }

    public int getN() {
        return n;
    }

    public String getName() {
        return name;
    }

    public int getP() {
        return p;
    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        stb.append(name).append("\n");
        for (int i=1;i<=n;i++) {
            for (int j=1;j<=n;j++) {
                stb.append("[").append(dist[i][j]).append("]");
            }
            stb.append("\n");
        }
        return stb.toString();
    }
}
