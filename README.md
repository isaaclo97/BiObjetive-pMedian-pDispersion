![visitor badge](https://visitor-badge.glitch.me/badge?page_id=isaaclo97.BiObjetive-pMedian-pDispersion&color=be54c6&style=flat&logo=Github)
![Manintained](https://img.shields.io/badge/Maintained%3F-yes-green.svg)
![GitHub last commit (master)](https://img.shields.io/github/last-commit/isaaclo97/BiObjetive-pMedian-pDispersion)
![Starts](https://img.shields.io/github/stars/isaaclo97/BiObjetive-pMedian-pDispersion.svg)

# A reactive path relinking algorithm for solving the bi-objective p-Median and p-Dispersion problem

This paper deals with an interesting facility location problem known as the bi-objective p-Median and p-Dispersion problem (BpMD problem). The BpMD problem seeks to locate p facilities to service a set of n demand points, and the goal is to minimize the total distance between facilities and demand points and, simultaneously, maximize the minimum distance between all pairs of hosted facilities. The problem is addressed with a novel path relinking approach, called reactive path relinking, which hybridizes two of the most extended path relinking variants: interior path relinking and exterior path relinking. Additionally, the proposal is adapted to a multi-objective perspective for finding a good approximation of the Pareto front. Computational results prove the superiority of the proposed algorithm over the best procedures found in the literature.

Paper link: [https://link.springer.com/article/10.1007/s00500-023-07994-4](https://link.springer.com/article/10.1007/s00500-023-07994-4) <br>
DOI: [https://doi.org/10.1007/s00500-023-07994-4](https://doi.org/10.1007/s00500-023-07994-4) <br>
Impact Factor: 3.732 (2021)  <br>
Quartil: Q2 - 65/145 - Computer Science, Artificial Intelligence - (2021) <br>

## Datasets

* [pmed instances](http://people.brunel.ac.uk/~mastjjb/jeb/orlib/files/)
* [D250-350 instances](https://www.isi.edu/~lerman/downloads/digg2009.html)
* [kmedian instances](https://resources.mpi-inf.mpg.de/departments/d1/projects/benchmarks/UflLib/kmedian.html)

All txt format instances can be found in instances folder (some of the instances are in .zip extension due to space demanding).

## Source code

### EpsilonConstraint
Contains the implementation of the exact models using Gurobi. To use it, it is necessary to have Gurobi installed on the device.

### MOFLP_MOEAMaven
MOEA Framework is a free and open source Java library for developing and experimenting with multiobjective evolutionary algorithms (MOEAs), this project contains instances parser (be careful, sometimes it is necessary to modify some fields for the correct instance format) of all the algorithms proposed to perform the comparison.

### MOMetricsMaven
Once the algorithm returns the different pareto fronts, this project is used to analyze the different metrics explained in the article. Given two folders with the pareto fronts of an instance, it reports the metrics obtained with which conclusions can be drawn.

### PMPD
This proyect has our algorithmic proposal related to Reactive Path Relinking and the different methods as local searches. This code is compiled and build in .jar format. BiObjetive-pMedian-pDispersion.jar is in the root of the project for use without the need to buid the code.

## Results

* SOCO_Latex_Tables.xlsx

### Pareto fronts

Our final pareto fronts can be found in pareto folder.
All the pareto front generated can be found in [./code/MOMetricsMaven/test](./code/MOMetricsMaven/test) 

## Executable

You can just run the BiObjetive-pMedian-pDispersion.jar as follows.

```
java -jar BiObjetive-pMedian-pDispersion.jar <instances folder> <iterations> <k>
```

For instance:

```
java -jar BiObjetive-pMedian-pDispersion.jar "instances/pmed"
java -jar BiObjetive-pMedian-pDispersion.jar "instances/kmedian" 10
java -jar BiObjetive-pMedian-pDispersion.jar "instances/D250-350" 10 0.5
```

instances folder per default is ../../instances/kmedian<br>
Iterations per default is 100.<br>
k per default is 0.75<br>

## Extra tool

### Pareto Comparator

The file paretoComparator.py analice two different pareto folders with different methods.

- Generate PDF file with the different graphics (RPRvsNSGA2.pdf)
- Extract PGN files individually per instances (folder images)
- Analize just one pareto file in folder

The requirements are:

| Package        | Minimum Version | Installation  |
|-----------| -----------------|-----------------|
| plotly | >=5.8.1	 | pip install plotly |
| fpdf | >=1.7.2    | pip install fpdf |
| numpy | >=1.22.4	| pip install numpy |


The paretos folder must have the same file names.

## Cite

Please cite our paper if you use it in your own work:

Bibtext
```
@Article{Lozano-Osorio2023,
author={Lozano-Osorio, I.
and S{\'a}nchez-Oro, J.
and L{\'o}pez-S{\'a}nchez, A. D.
and Duarte, A.},
title={A reactive path relinking algorithm for solving the bi-objective p-Median and p-Dispersion problem},
journal={Soft Computing},
year={2023},
month={Mar},
day={31},
issn={1433-7479},
doi={10.1007/s00500-023-07994-4},
url={https://link.springer.com/article/10.1007/s00500-023-07994-4}
}
```

RIS
```
TY  - JOUR
AU  - Lozano-Osorio, I
AU  - Sánchez-Oro, J
AU  - López-Sánchez, A D
AU  - Duarte, A
TI  - A reactive path relinking algorithm for solving the bi-objective p-Median
      and p-Dispersion problem
T2  - Soft Computing
PY  - 2023
DA  - 2023/3/31
SN  - 1433-7479
DO  - 10.1007/s00500-023-07994-4
UR  - https://link.springer.com/article/10.1007/s00500-023-07994-4
UR  - http://dx.doi.org/10.1007/s00500-023-07994-4
ER  - 
```

AMA Style
```
Lozano-Osorio I, Sánchez-Oro J, López-Sánchez AD, Duarte A. A reactive path relinking algorithm for solving the bi-objective p-Median and p-Dispersion problem. Soft Computing. Published online 31 March 2023. doi:10.1007/s00500-023-07994-4
```

Chicago/Turabian Style
```
Lozano-Osorio, I., J. Sánchez-Oro, A. D. López-Sánchez, and A. Duarte. ‘A Reactive Path Relinking Algorithm for Solving the Bi-Objective p-Median and p-Dispersion Problem’. Soft Computing, 31 March 2023. https://doi.org/10.1007/s00500-023-07994-4.
```
