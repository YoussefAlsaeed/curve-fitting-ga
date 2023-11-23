package curve_fitting_ga;

import java.util.List;

public class curveFittingGA {
	
	List<List<Point>> points; // Array of items with weight and value
    List<Integer> degrees; 
    int populationSize = 50;
    int maxGenerations = 100;
    double mutationRate = 0.1;
    double crossoverRate = 0.8;
    static int testCaseNum=0;

}
