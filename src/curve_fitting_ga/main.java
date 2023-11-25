package curve_fitting_ga;

import java.util.ArrayList;
import java.util.List;

public class main {
	public static void main(String[] args) {
	    String filename = "curve_fitting_input.txt";
	    FileReader fileReader = new FileReader(filename);

	    try {
	        if (fileReader.isOpen()) {
	            int numDatasets = fileReader.readNumOfDatasets();
	            List<List<Point>> allDatasets = new ArrayList<>();
	            List<Integer> degrees = new ArrayList<>();

	            for (int i = 0; i < numDatasets; i++) {
	                List<Point> points = new ArrayList<>();

	                int polynomialDegree = fileReader.readDataset(points);
	                
                    for (int j = 0; j < points.size(); j++) {
						System.out.println(points.get(j).getX() + " " + points.get(j).getY());
					}	
                    
                    System.out.println("------------------------------------------");

	                if (polynomialDegree != 0) {
	                	
                        Point[] pointsArray = points.toArray(new Point[0]);
                                             
	                    allDatasets.add(points);
	                    degrees.add(polynomialDegree);

	                    CurveFittingGA gaSolver = new CurveFittingGA(polynomialDegree, pointsArray);
	                    gaSolver.geneticAlgorithm();
	                } else {
	                    System.err.println("Error reading dataset " + (i + 1));
	                    break;
	                }
	            }
	        } else {
	            System.err.println("Error: File is not open.");
	        }
	    } catch (Exception e) {
	        System.err.println("Error: " + e.getMessage());
	    } finally {
	        fileReader.close();
	    }
	}

}
