package curve_fitting_ga;

public class Chromosome {
	
	double fitness;
	double[] coefficients;
	
	public Chromosome(int degree)
	{
		this.fitness = 0.0;
		this.coefficients = new double [degree + 1];
	}
}