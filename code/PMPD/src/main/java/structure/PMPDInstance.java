package structure;

import grafo.optilib.structure.Instance;

import java.io.*;
import java.util.Arrays;

public class PMPDInstance implements Instance {

    private String name;
    private int nodes,edges,facilities;
    private long startTime;
    private long minutes = 3600;
    private double graph[][];
    private double alpha,k;
    private double distance;
    private double minValueToOneNode = 0x3f3f3f, maxValueToOneNode, minValueToAllNodes, maxValueToAllNodes;
    private int mapValues[];

    public PMPDInstance(String path) {
        if(path.contains("pmed"))
            readInstance(path);
        else if(path.contains("D_250") || path.contains("D_350"))
            readInstanceChema(path);
        else
            readInstanceKMedian(path);
        startTime = System.nanoTime();
    }

    public void readInstanceKMedian(String path) {
        this.name = path.substring(path.lastIndexOf('\\') + 1);
        int minDistance = Integer.MAX_VALUE;
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
            nodes = Integer.parseInt(l[0]);
            mapValues = new int[nodes];
            edges = nodes*nodes;
            facilities = Integer.parseInt(l[2]);
            graph = new double[nodes][nodes];
            for(int i=0; i<nodes;i++){
                line = br.readLine();
                l = line.split(" ");
                for(int j=0; j<l.length-2;j++){
                    int start = i;
                    int end = j;
                    int cost = Integer.parseInt(l[j+2]);
                    graph[start][end]=cost;
                    graph[end][start]=cost;
                    if(cost!=0)
                        minDistance = Math.min(cost,minDistance);
                }
            }
            for (int i = 0; i < nodes; i++) {
                double maxValue = 0;
                double minValue= 0x3f3f3f3f;
                for (int j = 0; j < nodes; j++) {
                    if(i==j) continue;
                    minValue = Math.min(minValue,graph[i][j]);
                    maxValue = Math.max(maxValue,graph[i][j]);
                }
                maxValueToAllNodes+=maxValue;
                minValueToAllNodes+=minValue;
                minValueToOneNode = Math.min(minValueToOneNode,minValue);
                maxValueToOneNode = Math.max(maxValueToOneNode,maxValue);
            }
            br.close();
        }
        catch (IOException e){
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
            nodes = Integer.parseInt(l[0]);
            edges = nodes*nodes;
            facilities = Integer.parseInt(l[1]);
            graph = new double[nodes][nodes];
            mapValues = new int[nodes];
            while ((line = br.readLine()) != null) {
                l = line.split("\t");
                int start = Integer.parseInt(l[0])-1;
                int end = Integer.parseInt(l[1])-1;
                double cost = Double.parseDouble(l[2]);
                graph[start][end]=cost;
                graph[end][start]=cost;
            }
            br.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        for (int i = 0; i < nodes; i++) {
            double maxValue = 0;
            double minValue= 0x3f3f3f3f;
            for (int j = 0; j < nodes; j++) {
                if(i==j) continue;
                minValue = Math.min(minValue,graph[i][j]);
                maxValue = Math.max(maxValue,graph[i][j]);
            }
            maxValueToAllNodes+=maxValue;
            minValueToAllNodes+=minValue;
            minValueToOneNode = Math.min(minValueToOneNode,minValue);
            maxValueToOneNode = Math.max(maxValueToOneNode,maxValue);
        }
    }

    @Override
    public void readInstance(String path) {
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
            String[] l = line.split(" ");
            nodes = Integer.parseInt(l[1]);
            mapValues = new int[nodes];
            edges = Integer.parseInt(l[2]);
            facilities = Integer.parseInt(l[3]);
            graph = new double[nodes][nodes];
            for(int i=0; i<nodes;i++){
                Arrays.fill(graph[i],0x3f3f3f3f);
            }

            while ((line = br.readLine()) != null) {
                l = line.split(" ");
                int start = Integer.parseInt(l[1])-1;
                int end = Integer.parseInt(l[2])-1;
                int cost = Integer.parseInt(l[3]);
                graph[start][end]=cost;
                graph[end][start]=cost;
            }
            br.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        FloydWarshall();

        for (int i = 0; i < nodes; i++) {
            double maxValue = 0;
            double minValue= 0x3f3f3f3f;
            for (int j = 0; j < nodes; j++) {
                if(i==j) continue;
                minValue = Math.min(minValue,graph[i][j]);
                maxValue = Math.max(maxValue,graph[i][j]);
            }
            maxValueToAllNodes+=maxValue;
            minValueToAllNodes+=minValue;
            minValueToOneNode = Math.min(minValueToOneNode,minValue);
            maxValueToOneNode = Math.max(maxValueToOneNode,maxValue);
        }
    }

    public void FloydWarshall(){
        for (int k = 0; k < nodes; k++)
            for (int i = 0; i < nodes; i++)
                for (int j = 0; j < nodes; j++)
                    graph[i][j]=Math.min(graph[i][j],graph[i][k]+graph[k][j]);
    }

    public long getStartTime() {
        return startTime;
    }

    public String getName() {
        return name;
    }

    public int getNodes() {
        return nodes;
    }

    public int getFacilities() {
        return facilities;
    }

    public double[][] getGraph() {
        return graph;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getMinValueToOneNode() {
        return minValueToOneNode;
    }

    public double getMaxValueToOneNode() {
        return maxValueToOneNode;
    }

    public double getMinValueToAllNodes() {
        return minValueToAllNodes;
    }

    public double getMaxValueToAllNodes() {
        return maxValueToAllNodes;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public int[] getMapValues() {
        return mapValues;
    }

    public void setMapValues(int id, int value) {
        this.mapValues[id] = value;
    }
}
