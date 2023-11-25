package curve_fitting_ga;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class CurveFittingGA {
	
	//initializing some variables
    private int polynomialDegree;
    private Point[] points;
    private int populationSize = 100;
    private int generations = 100;
    private Chromosome[] population;
    private Chromosome[] parents;
    private Chromosome[] children;
    private int eliteCount =30;
    private static int testcase = 0;
    private double upperBound = 10;
    private double lowerBound = -10;
    private double Pc = 0.7;
    private double Pm = 0.01;
    private int b = 1;  // for nonunifrom mutation
    private int nonElite = populationSize - eliteCount;
    
    public CurveFittingGA(int polynomialDegree, Point[] points) {
    	this.testcase++;
        this.polynomialDegree = polynomialDegree;
        this.points = points;
        this.population = new Chromosome[populationSize];
        this.parents = new Chromosome[nonElite];
        this.children = new Chromosome[nonElite];
        
        
        // Initializing population
        for (int i = 0; i < populationSize; i++) {
            population[i] = new Chromosome(polynomialDegree);

            for (int j = 0; j < polynomialDegree + 1; j++) {
                population[i].coefficients[j] = (20 * Math.random()) - 10;
                // round to 2 decimal points
                population[i].coefficients[j] = Math.round(100 * population[i].coefficients[j]) / 100.0;
            }
        }

        // Initializing parents and children
        for (int i = eliteCount; i < populationSize; i++) {
            parents[i - eliteCount] = new Chromosome(polynomialDegree);
            children[i - eliteCount] = new Chromosome(polynomialDegree);
        }

    }
    
    public void writeToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("results.txt", true))) {
            writer.println("Dataset: " + testcase);
            for (int i = 0; i <= polynomialDegree; i++) {
                writer.println("Coefficient " + i + " = " + population[0].coefficients[i]);
            }
            writer.println("Mean Square Error = " + population[0].fitness);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Call functions by order
     * Fitness  - > Sort - > selection - > crossover - > mutation -> replacement
     */
    public void geneticAlgorithm() {
        for (int i = 0; i < generations; i++) {
            mseFitness();
            sortPopulation(population);
            tournamentSelection();
            twoPointCrossover();
            nonUniformMutation();
            elitistReplacement();
        }        
        solveBestSolution();
    }
    
    
    //To get best solution in population and write it into file
    
    public void solveBestSolution()
    {
        mseFitness();
        sortPopulation(population);
        writeToFile();
    }

    //sort the population according to fitness
    public void sortPopulation(Chromosome[] population) {
        Arrays.sort(population, Comparator.comparingDouble(chromosome -> chromosome.fitness));
    }

    public void mseFitness()
    {
    	//Perform mse equation on each chromosome
        double total = 0.0;

        for (int i = 0; i < populationSize; i++)
        {
            for (int j = 0; j < points.length; j++)
            {
                double y = 0.0;

                for (int l = 0; l < polynomialDegree + 1; l++)
                {
                    y += population[i].coefficients[l] * (Math.pow(points[j].x,l));
                }

                total += Math.pow((y - points[j].y),2);
            }
            population[i].fitness = (total / points.length);
            total = 0.0;
        }

    }

    public void tournamentSelection() {
        for (int i = 0; i < populationSize - eliteCount; i++) {
        	
        	// Select from population
            int contender1 = eliteCount + (int) (Math.random() * (populationSize - eliteCount));
            int contender2 = eliteCount + (int) (Math.random() * (populationSize - eliteCount));

            int selectedContender;
            
            // Contender with best fitness wins 
            
            if (population[contender1].fitness < population[contender2].fitness) {
                selectedContender = contender1;
            } else {
                selectedContender = contender2;
            }
            
            // Copy the winning contender to parents array
            parents[i].coefficients = Arrays.copyOf(population[selectedContender].coefficients, population[selectedContender].coefficients.length);
        }
    }
    
    public void nonUniformMutation() {
    	
    	// To perform mutation on each gene 
    	
        for (int i = 0; i < populationSize - eliteCount; i++) {
            for (int j = 0; j <= polynomialDegree; j++) {
            	
            	// Do mutation if random value is less than or qual to pm
            	
                if (Math.random() <= Pm) {
                    // Calculate the distance from the current value to the lower and upper bounds
                    double distanceToLowerBound = children[i].coefficients[j] - lowerBound;
                    double distanceToUpperBound = upperBound - children[i].coefficients[j];

                    // n3rf e7na lower wala upper bound
                    boolean mutateTowardsLowerBound = Math.random() <= 0.5;
                    double distanceToMutate;

                    if (mutateTowardsLowerBound) {
                        distanceToMutate = distanceToLowerBound;
                    } else {
                        distanceToMutate = distanceToUpperBound;
                    }

                    // Calculate the delta value using the non-uniform mutation formula
                    double delta = distanceToMutate * (1 - Math.pow(Math.random(), 1 - Math.pow(((double) testcase / (double) generations), b)));
                    delta = Math.round(100 * delta) / 100.0;

                    if (mutateTowardsLowerBound) {
                        children[i].coefficients[j] -= delta;
                    } else {
                        children[i].coefficients[j] += delta;
                    }
                    children[i].coefficients[j] = Math.round(100 * children[i].coefficients[j]) / 100.0;
                }
            }
        }
    }

    public void twoPointCrossover() {
        for (int i = 0; i < populationSize - eliteCount; i += 2) {
            double doCrossover = Math.random();
            
            // Check lw hn3ml crossover wla la 7sab el pc
            
            if (doCrossover <= Pc) {
            	// random 2 points
                int point1 = (int) (Math.random() * (polynomialDegree + 1));
                int point2 = (int) (Math.random() * (polynomialDegree + 1));

                //point boundaries check
                while (point1 == point2 || (point1 == 0 && point2 == polynomialDegree)) {
                    point1 = (int) (Math.random() * (polynomialDegree + 1));
                    point2 = (int) (Math.random() * (polynomialDegree + 1));
                }
                
                //awl point lazm tb2a as8r mn eltanya
                if (point1 > point2) {
                    int temp = point1;
                    point1 = point2;
                    point2 = temp;
                }
                // First child
                crossover(parents[i].coefficients, parents[i + 1].coefficients, children[i].coefficients, point1, point2);
                // Second child
                crossover(parents[i + 1].coefficients, parents[i].coefficients, children[i + 1].coefficients, point1, point2);
            } else {
            	// lw no crossover el parents hyfdlo zy ma homa
                children[i].coefficients = Arrays.copyOf(parents[i].coefficients, parents[i].coefficients.length);
                children[i + 1].coefficients = Arrays.copyOf(parents[i + 1].coefficients, parents[i + 1].coefficients.length);
            }
        }
    }

    private void crossover(double[] parent1, double[] parent2, double[] child, int point1, int point2) {
    	
    	// copy ll offspring from parent1 from 0 to point1
        System.arraycopy(parent1, 0, child, 0, point1);
        
    	// copy ll offspring from parent2 from point1 to point2
        System.arraycopy(parent2, point1, child, point1, point2 - point1);
        
    	// copy ll offspring from parent1 from point2 to end
        System.arraycopy(parent1, point2, child, point2, parent1.length - point2);
    }

    public void elitistReplacement() {
    	// copy best children to replace least fit in population while preserving the elites (loop btbda2 mn elite count)
        for (int i = eliteCount; i < populationSize; i++) {
            population[i].coefficients = children[i - eliteCount].coefficients.clone();
            population[i].fitness = children[i - eliteCount].fitness;
        }
    }

}