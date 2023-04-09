package algorithms;

import grafo.optilib.metaheuristics.Algorithm;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.metaheuristics.Improvement;
import grafo.optilib.results.Result;
import grafo.optilib.structure.Solution;
import structure.PMPDInstance;
import structure.PMPDSolution;
import structure.Pareto;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class RPR implements Algorithm<PMPDInstance> {

	final Constructive<PMPDInstance, PMPDSolution> constructive;
	Improvement<PMPDSolution> improvement, pathRelinking;

	// The output directory path, used for testing purposes only
	private final Integer iterations;
	private double distance;
	private double k;
	private String folderOutput;

	public RPR(Constructive<PMPDInstance, PMPDSolution> constructive, Integer iterations, double distance, Improvement<PMPDSolution> improve, Improvement<PMPDSolution> pathRelinking, double k, String folderOutput){
		this.constructive = constructive;
		this.iterations = iterations;
		this.improvement = improve;
		this.distance = distance;
		this.pathRelinking = pathRelinking;
		this.k = k;
		this.folderOutput = folderOutput;
	}

	@Override
	public Result execute(PMPDInstance instance){
		final long startTime = instance.getStartTime();
		long inicio = System.currentTimeMillis();
		instance.setDistance(this.distance);
		instance.setK(k);
		Pareto.reset(); //Hacer para cada algoritmo
		PMPDSolution sol = null;
		double initial = 0.00;
		double tiempo = 0.00;
		int time = 3600; //3600 10
		double alphas[] = {0.5,1.0,0.0,0.01,0.99,0.02,0.98,0.03,0.97,0.04,0.96,0.05,0.95,0.06,0.94,0.07,0.93,0.08,0.92,0.09,0.91,0.1,0.9,0.11,0.89,0.12,0.88,0.13,0.87,0.14,0.86,0.15,0.85,0.16,0.84,0.17,0.83,0.18,0.82,0.19,0.81,0.2,0.8,0.21,0.79,0.22,0.78,0.23,0.77,0.24,0.76,0.25,0.75,0.26,0.74,0.27,0.73,0.28,0.72,0.29,0.71,0.3,0.7,0.31,0.69,0.32,0.68,0.33,0.67,0.34,0.66,0.35,0.65,0.36,0.64,0.37,0.63,0.38,0.62,0.39,0.61,0.4,0.6,0.41,0.59,0.42,0.58,0.43,0.57,0.44,0.56,0.45,0.55,0.46,0.54,0.47,0.53,0.48,0.52,0.49,0.51};
		for(int i=0; i<100 && tiempo<(time/2);i++) {
			instance.setAlpha(alphas[i]);
			sol = constructive.constructSolution(instance);
			sol.setStartTime(inicio);
			sol.checkFeasible();
			sol.setTime((long) (time/2));
			initial+=0.01;
			Pareto.add(sol);
			improvement.improve(sol);
			long fin = System.currentTimeMillis();
			tiempo = (double) ((fin - inicio)/1000);
		}
		initial = 0.00;
		for(int i=0; i<iterations && tiempo<time;i++) {
			sol.getInstance().setAlpha(alphas[i]);
			sol.setTime((long) (time));
			pathRelinking.improve(sol);
			sol.setTime((long) (time));
			sol.checkFeasible();
			improvement.improve(sol);
			initial+=0.01; //0.10
			long fin = System.currentTimeMillis();
			tiempo = (double) ((fin - inicio)/1000);
		}

		long timeToSolution = TimeUnit.MILLISECONDS.convert((long) (System.nanoTime() - startTime), TimeUnit.NANOSECONDS);
		Result r = new Result(instance.getName());
		double seconds = timeToSolution / 1000.0;
		System.out.println("Time (s): " + seconds);

		r.add("Time (s)", seconds);
		r.add("# Constructions", iterations);

		String path = "./"+folderOutput+"/";
		File folder = new File(path);
		folder.mkdirs();
		String name = instance.getName().replace("/","").replace(".","").replace("txt",".txt");
		Pareto.saveToFile(path+name);
		return r;
	}

	@Override
	public Solution getBestSolution() {
		return null;
	}

	@Override
	public String toString(){
		return this.getClass().getSimpleName() + "(" + constructive + ")";
	}
}
