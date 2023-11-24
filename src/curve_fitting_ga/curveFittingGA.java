package curve_fitting_ga;
import java.io.*;

public class CurveFittingGA {
	
    private int degree;
    private Point[] points;
    private int n;
    private int populationSize = 60;
    private int iterations = 5;
    private int eliteIndividuals = 2;
    private Chromosome[] population;
    private Chromosome[] parents;
    private Chromosome[] offsprings;
    private int elite_individuals =2;
    private static int generation = 0;
    private double upper = 10;
    private double lower = -10;
    private double Pc = 0.6;
    private double Pm = 0.1;
    private int b = 1;
    private Chromosome[] elites;    
    
    public CurveFittingGA(int degree, Point[] points) {
        this.degree = degree;
        this.points = points;
        this.n = points.length;
        this.population = new Chromosome[populationSize];
        this.parents = new Chromosome[populationSize - eliteIndividuals];
        this.offsprings = new Chromosome[populationSize - eliteIndividuals];
        this.elites = new Chromosome[eliteIndividuals];
        Initialization();
    }

    public void Initialization()
    {
        for (int i = 0; i < populationSize; i++)
        {
            population[i] = new Chromosome(degree);
            if (i >= elite_individuals)
            {
                parents[i - elite_individuals] = new Chromosome(degree);
                offsprings[i - elite_individuals] = new Chromosome(degree);
            }

            for (int j = 0; j <= degree; j++)
            {
                population[i].coefficients[j] = (20 * Math.random()) - 10;
                population[i].coefficients[j] = Math.round(100 * population[i].coefficients[j]) / 100.0;
            }
        }

        for (int i = 0; i < elite_individuals; i++)
        {
            elites[i] = new Chromosome(degree);
        }
    }

    public void Sort(Chromosome[] c) {

        Chromosome temp = new Chromosome(degree);

        for (int i = 0; i < populationSize - 1; i++) {

            int min_idx = i;
            for (int j = i + 1; j < populationSize; j++)
                if (c[j].fitness < c[min_idx].fitness)
                    min_idx = j;


            temp.coefficients = c[min_idx].coefficients.clone();
            temp.fitness = c[min_idx].fitness;

            c[min_idx].coefficients = c[i].coefficients.clone();
            c[min_idx].fitness = c[i].fitness;

            c[i].coefficients = temp.coefficients.clone();
            c[i].fitness = temp.fitness;
        }
    }

    public void mseFitness()
    {
        double total = 0.0;

        for (int i = 0; i < populationSize; i++)
        {
            for (int j = 0; j < n; j++)
            {
                double ycalc = 0.0;

                for (int k = 0; k <= degree; k++)
                {
                    ycalc += population[i].coefficients[k] * (Math.pow(points[j].x,k));
                }

                total += Math.pow((ycalc - points[j].y),2);
                total = Math.round(100 * total) / 100.0;
            }
            population[i].fitness = total / n;
            total = 0;
        }

        Sort(population);
    }

    public void Selection()
    {
        int selector;
        int selector2;

        for (int i = 0; i < populationSize - elite_individuals; i++)
        {
            selector = ((int) (Math.random() * (populationSize - elite_individuals))) + elite_individuals;
            selector2 = ((int) (Math.random() * (populationSize - elite_individuals))) + elite_individuals;

            if (population[selector].fitness < population[selector2].fitness)
            {
                parents[i].coefficients = population[selector].coefficients.clone();
            }

            else
            {
                parents[i].coefficients = population[selector2].coefficients.clone();
            }
        }

    }

    public void twoPointCrossover() {
        int point1 = 0;
        int point2 = 0;
        int temp = 0;

        for (int i = 0; i < populationSize - elite_individuals; i += 2) {
            double decision = Math.random();

            if (decision <= Pc) {
                point1 = (int) (Math.random() * (degree + 1));
                point2 = (int) (Math.random() * (degree + 1));
                if (point1 > point2) {
                    temp = point1;
                    point1 = point2;
                    point2 = temp;
                }

                // Ensure points are not equal
                while (point1 == point2 || (point1 == 0 && point2 == degree)) {
                    point1 = (int) (Math.random() * (degree + 1));
                    point2 = (int) (Math.random() * (degree + 1));

                    if (point1 > point2) {
                        temp = point1;
                        point1 = point2;
                        point2 = temp;
                    }                    
                }

                // Perform two-point crossover
                System.arraycopy(parents[i].coefficients, 0, offsprings[i].coefficients, 0, point1);
                System.arraycopy(parents[i + 1].coefficients, 0, offsprings[i + 1].coefficients, 0, point1);

                System.arraycopy(parents[i + 1].coefficients, point1, offsprings[i].coefficients, point1, point2 - point1);
                System.arraycopy(parents[i].coefficients, point1, offsprings[i + 1].coefficients, point1, point2 - point1);

                System.arraycopy(parents[i].coefficients, point2, offsprings[i].coefficients, point2, degree + 1 - point2);
                System.arraycopy(parents[i + 1].coefficients, point2, offsprings[i + 1].coefficients, point2, degree + 1 - point2);
            } else {
                offsprings[i].coefficients = parents[i].coefficients.clone();
                offsprings[i + 1].coefficients = parents[i + 1].coefficients.clone();
            }
        }
    }

    public void nonuniformMutation()
    {
        for (int i = 0; i < populationSize - elite_individuals; i++)
        {
            for (int j = 0; j <= degree; j++)
            {
                double decision = Math.random();
                if (decision <= Pm)
                {
                    double Lx = offsprings[i].coefficients[j] - lower;
                    double Ux = upper - offsprings[i].coefficients[j];
                    double y;
                    double delta;

                    double decision2 = Math.random();

                    if (decision2 <= 0.5)
                        y = Lx;
                    else
                        y = Ux;

                    double r = Math.random();
                    delta = y * (1 - Math.pow(r,1 - Math.pow(((double)generation/(double)iterations),b)));
                    delta = Math.round(100 * delta) / 100.0;

                    if (y == Lx)
                        offsprings[i].coefficients[j] -= delta;
                    else
                        offsprings[i].coefficients[j] += delta;
                    offsprings[i].coefficients[j] = Math.round(100 * offsprings[i].coefficients[j]) / 100.0;
                }
            }
        }
    }

    public void elitistReplacement()
    {
        for (int i = elite_individuals; i < populationSize; i++)
        {
            population[i].coefficients = offsprings[i - elite_individuals].coefficients.clone();
        }

        for (int i = 0; i < populationSize; i++)
        {
            population[i].fitness = 0;
        }
    }

    public void writeToFile() {
        try {
            FileWriter fw = new FileWriter("output.txt", true);
            fw.write("In test case #" + generation + "\n");
            for (int j = 0; j <= degree; j++) {
                fw.write("a" + j + " = " + population[0].coefficients[j] + "\n");
            }
            fw.write("MSE = " + population[0].fitness + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void geneticAlgorithm() {
        for (int j = 0; j < iterations; j++) {
            generation++;
            mseFitness();
            Selection();
            twoPointCrossover();
            nonuniformMutation();
            elitistReplacement();
        }
        mseFitness();
        Sort(population);
        writeToFile();
        generation = 0;
    }
}