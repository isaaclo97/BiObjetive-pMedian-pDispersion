package structure;

import grafo.optilib.structure.Solution;

import java.util.*;

@SuppressWarnings("Duplicates")
public class PMPDSolution implements Solution {

    private PMPDInstance instance;
    private double alpha;
    private double median, dispersion;
    private double medianNormalized, dispersionNormalized;
    private boolean updated;
    private HashSet<Integer> S; //selected nodes
    private HashMap<Integer,Integer> updateFO = new HashMap<>();
    private HashSet<Integer> remaining;
    private long time = 100000000;
    private long startTime = 100000000;

    public PMPDSolution(PMPDInstance instance) {
        S = new HashSet<>();
        this.instance = instance;
        this.updated = true;
        remaining = new HashSet<>();
        for(int i=0; i<instance.getNodes();i++)
            remaining.add(i);
    }
    public PMPDSolution(PMPDSolution solution) {
        copy(solution);
    }

    public void copy(PMPDSolution solution){
        this.instance = solution.getInstance();
        this.S = new HashSet<>(solution.getS());
        this.updated = solution.isUpdated();
        this.median = solution.getMedian();
        this.dispersion = solution.getDispersion();
        this.alpha = solution.getAlpha();
        this.remaining = new HashSet<>(solution.remaining);
        this.time = solution.time;
        this.startTime = solution.startTime;
    }

    public double evaluateMedianoAddElement(int element) {
        if(!updated)
            evaluateMediano(); //Update distance vector if required
        updateFO.clear();
        int res = 0;
        for (Integer elem:remaining) {
            if (element==elem) continue;
            double valueElement = instance.getGraph()[elem][element];
            int mapValue = instance.getMapValues()[elem];
            if(mapValue>valueElement) res += valueElement;
            else res+=mapValue;
        }
        dispersionNormalized = (double)(res-instance.getMinValueToOneNode())/(double)(instance.getMaxValueToOneNode()- instance.getMinValueToOneNode());
        return this.dispersion = res;
    }
    public double evaluateMediano() {
            int res = 0;
            for (int i = 0; i < instance.getNodes(); i++) { //Nodos a facilities mas cercana
                if (S.contains(i)) continue;
                double minDistance = 0x3f3f3f;
                for (int selected : S) {
                    minDistance = Math.min(minDistance, instance.getGraph()[i][selected]);
                }
                instance.setMapValues(i,(int)minDistance);
                res += minDistance;
            }
        //Normalization (x-xmin)/(xmax-xmin)
        medianNormalized = (double)(res-instance.getMinValueToAllNodes())/(double)(instance.getMaxValueToAllNodes()- instance.getMinValueToAllNodes());
        return this.median = res;
    }

    public double evaluateDispersion() {
        double res = Integer.MAX_VALUE;
        Object[] facilities = S.toArray();
        for (int i=0; i<facilities.length;i++) {
            for (int j=i+1; j<facilities.length;j++) {
                res = Math.min(res, instance.getGraph()[(int)facilities[i]][(int)facilities[j]]);
            }
        }
        //Normalization (x-xmin)/(xmax-xmin)
        dispersionNormalized = (double)(res-instance.getMinValueToOneNode())/(double)(instance.getMaxValueToOneNode()- instance.getMinValueToOneNode());
        return this.dispersion = res;
    }

    public double evaluate(int elem){
        evaluateMedianoAddElement(elem);
        this.addToSolution(elem);
        evaluateDispersion();
        this.removeToSolution(elem);
        return  (this.alpha*this.medianNormalized+((1-this.alpha)*(-this.dispersionNormalized)));
    }

    public double evaluate(){
        if(updated){
            evaluateMediano();
            evaluateDispersion();
            updated = false;
        }
        return  (this.alpha*this.medianNormalized+((1-this.alpha)*(-this.dispersionNormalized)));
    }


    public double onlyDispersion(){
        if(updated){
            evaluateDispersion();
            updated = false;
        }
        return -this.dispersion;
    }

    public PMPDInstance getInstance() {
        return instance;
    }

    public HashSet<Integer> getS() {
        return S;
    }

    public void addToSolution(int node){
        updated = true;
        S.add(node);
        remaining.remove(node);
    }
    public void removeToSolution(int node){
        updated = true;
        S.remove(node);
        remaining.add(node);
    }
    public boolean isInSolution(int node){
        return S.contains(node);
    }

    public void checkFeasible(){
        if(this.getS().size()!=this.instance.getFacilities()){
            try {
                throw new RuntimeException("BUG NO FEASIBLE");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public double getOF(int i) {
        if(updated){
            evaluateMediano();
            evaluateDispersion();
            updated = false;
        }
        if(i==0){
            return median;
        }
        return dispersion;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getMedian() {
        return median;
    }

    public double getDispersion() {
        return dispersion;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setTime(long time){
        this.time = time;
    }
    public double getTime(){
        return this.time;
    }
    public void setStartTime(long startTime){
        this.startTime = startTime;
    }
    public double getStartTime(){
        return this.startTime;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PMPDSolution that = (PMPDSolution) o;
        return Double.compare(that.alpha, alpha) == 0 &&
                Double.compare(that.median, median) == 0 &&
                Double.compare(that.dispersion, dispersion) == 0 &&
                Double.compare(that.medianNormalized, medianNormalized) == 0 &&
                Double.compare(that.dispersionNormalized, dispersionNormalized) == 0 &&
                updated == that.updated &&
                Objects.equals(instance, that.instance) &&
                Objects.equals(S, that.S);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instance, alpha, median, dispersion, medianNormalized, dispersionNormalized, updated, S);
    }
}
