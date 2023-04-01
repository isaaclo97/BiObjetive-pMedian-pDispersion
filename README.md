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

* [Instances pMed](http://people.brunel.ac.uk/~mastjjb/jeb/orlib/files/)
* [Instances D250-350](https://www.isi.edu/~lerman/downloads/digg2009.html)

All txt format instances can be found in instances folder.

## Executable

You can just run the BiObjetive-pMedian-pDispersion.jar as follows.

```
java -jar BiObjetive-pMedian-pDispersion.jar
```

If you want new instances just replace folder instances.
Solution folder contains de pareto output per instances.

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
