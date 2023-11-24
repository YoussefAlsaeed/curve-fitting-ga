package curve_fitting_ga;

public class Chromosome {
	
	double fitness = 0.0;
	double[] coefficients;
	
	public Chromosome(int degree)
	{
		this.coefficients = new double [degree + 1];
	}
}