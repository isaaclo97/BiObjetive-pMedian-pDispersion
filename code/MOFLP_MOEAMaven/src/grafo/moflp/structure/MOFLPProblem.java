package moflp.structure;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//Clase de carga de instancias, factibilidad, evaluacion

public class MOFLPProblem implements Problem {

    private String name;
    private int nodes;
    private int edges;
    private int facilities;

    private double[][] distance;


    public MOFLPProblem(String path) {
        super();
        //load(path);
        readInstanceKMedian(path);
        //readInstanceChema(path);
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
            edges = nodes*nodes;
            facilities = Integer.parseInt(l[2]);
            distance = new double[nodes][nodes];
            for(int i=0; i<nodes;i++){
                Arrays.fill(distance[i],0x3f3f3f3f);
            }
            for(int i=0; i<nodes;i++){
                line = br.readLine();
                l = line.split(" ");
                for(int j=0; j<l.length-2;j++){
                    int start = i;
                    int end = j;
                    int cost = Integer.parseInt(l[j+2]);
                    distance[start][end]=cost;
                    distance[end][start]=cost;
                    if(cost!=0)
                        minDistance = Math.min(cost,minDistance);
                }
            }
            br.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        FloydWarshall();
    }

    //Cargar instancias
    private void load(String path) {
        try{
            name = path.substring(path.lastIndexOf('/')+1);
            BufferedReader bf = new BufferedReader(new FileReader(path));

            String line = bf.readLine();
            String[] l = line.split(" ");
            nodes = Integer.parseInt(l[1]);
            edges = Integer.parseInt(l[2]);
            facilities = Integer.parseInt(l[3]);
            distance = new double[nodes][nodes];

            for(int i=0; i<nodes;i++){
                Arrays.fill(distance[i],0x3f3f3f3f);
            }
            while ((line = bf.readLine()) != null) {
                l = line.split(" ");
                int start = Integer.parseInt(l[1])-1;
                int end = Integer.parseInt(l[2])-1;
                int cost = Integer.parseInt(l[3]);
                distance[start][end]=cost;
                distance[end][start]=cost;
            }
            FloydWarshall();
            bf.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
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
            distance = new double[nodes][nodes];

            while ((line = br.readLine()) != null) {
                l = line.split("\t");
                int start = Integer.parseInt(l[0])-1;
                int end = Integer.parseInt(l[1])-1;
                double cost = Double.parseDouble(l[2]);
                distance[start][end]=cost;
                distance[end][start]=cost;
            }
            br.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void FloydWarshall(){
        for (int k = 0; k < nodes; k++)
            for (int i = 0; i < nodes; i++)
                for (int j = 0; j < nodes; j++)
                    distance[i][j]=Math.min(distance[i][j],distance[i][k]+distance[k][j]);
    }

    @Override
    public String getName() {
        return "MOFLP";
    }

    @Override
    public int getNumberOfVariables() {
        return facilities;
    }

    //Cambiar objetivos
    @Override
    public int getNumberOfObjectives() {
        return 2;
    }

    //Cambiar restricciones
    @Override
    public int getNumberOfConstraints() {
        return 1;
    }

    //Cambiar evaluate
    @Override
    public void evaluate(Solution solution) {
        int[] facilities = EncodingUtils.getInt(solution); //getBinary, getInteger, revisar doc
        int pMedian = 0;
        double minDistToFacilities = Integer.MAX_VALUE;

        int repeated = isFeasible(facilities);
        //Objetivo 1 minima distancia entre todos
        for (int d = 0; d < nodes; d++) {
            double distToClosest = distanceToClosest(facilities, d);
            pMedian += distToClosest;
        }
        //Objetivo 2 maximizar la minima distancia entre facilities
        for (int i = 0;  i<facilities.length;i++) {
            for (int j = i+1;  j<facilities.length;j++) {
                double dist = distance[facilities[i]][facilities[j]];
                if (dist < minDistToFacilities) {
                    minDistToFacilities = dist;
                }
            }
            //System.out.print (facilities[i] + " ");
        }
        //System.out.println(pMedian + " " + -minDistToFacilities);

        solution.setObjective(0, pMedian);
        solution.setObjective(1, -minDistToFacilities);
        solution.setConstraint(0, repeated);
    }

    //Cambiar es factible, es interesante ver cuando es menos factible y cuando es más factible
    private int isFeasible(int[] facilites) {
        Set<Integer> selected = new HashSet<>();
        for (int facility : facilites) {
            selected.add(facility);
        }
        return facilites.length-selected.size();
    }

    //Cambiar segun problema
    private double distanceToClosest(int[] facilities, int d) {
        double minDist = Integer.MAX_VALUE;
        for (int fac : facilities) {
            double dist = distance[fac][d];
            if (dist < minDist) {
                minDist = dist;
            }
        }
        return minDist;
    }

    //Cambiar
    @Override
    public Solution newSolution() {
        Solution solution = new Solution(facilities, getNumberOfObjectives(), getNumberOfConstraints());
        for (int i = 0; i < facilities; i++) {
            solution.setVariable(i, EncodingUtils.newInt(0, nodes-1));
        }
        return solution;
    }

    @Override
    public void close() {

    }
}
