package curve_fitting_ga;

import java.util.ArrayList;
import java.util.List;

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
                    int[] polynomialDegree = new int[1]; 
                    
                    if (fileReader.readDataset(points, polynomialDegree)) {
                        allDatasets.add(points);
                        degrees.add(polynomialDegree[0]);
                        
                    } else {
                        System.err.println("Error reading dataset " + (i + 1));
                        break;
                    }
                }

                // Print or use the datasets as needed
                for (int i = 0; i < allDatasets.size(); i++) {
                    System.out.println("Dataset " + (i + 1) + ":");
                    List<Point> dataset = allDatasets.get(i);
                    System.out.println("Polynomial Degree: " + degrees.get(i));
                    System.out.println("Points:");
                    for (Point point : dataset) {
                        System.out.println("(" + point.getX() + ", " + point.getY() + ")");
                    }
                    System.out.println();
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
