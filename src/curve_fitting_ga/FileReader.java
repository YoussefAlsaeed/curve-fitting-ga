package curve_fitting_ga;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import curve_fitting_ga.Point;

public class FileReader {
    private Scanner scanner;

    public FileReader(String filename) {
        try {
            scanner = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            System.err.println("Error: Cannot open file " + filename);
            scanner = null;
        }
    }

    public boolean isOpen() {
        return scanner != null;
    }

    public int readNumOfDatasets() throws Exception {
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        } else {
            throw new Exception("Error reading the number of datasets.");
        }
    }

    public boolean readDataset(List<Point> points, int[] polynomialDegree) {
        if (!isOpen()) {
            System.err.println("Error: File is not open.");
            return false;
        }

        if (scanner.hasNextInt()) {
            int numPoints = scanner.nextInt();
            polynomialDegree[0] = scanner.nextInt();
            points.clear();

            for (int i = 0; i < numPoints; i++) {
                if (scanner.hasNextDouble()) {
                    double x = scanner.nextDouble();
                    if (scanner.hasNextDouble()) {
                        double y = scanner.nextDouble();
                        points.add(new Point(x, y));
                    } else {
                        System.err.println("Error reading point data.");
                        return false;
                    }
                } else {
                    System.err.println("Error reading point data.");
                    return false;
                }
            }
            return true;
        } else {
            System.err.println("Error reading the number of points and polynomial degree.");
            return false;
        }
    }

    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}