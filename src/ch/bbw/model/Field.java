package ch.bbw.model;

import javafx.geometry.Point2D;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Field {
    List<Point2D> point2DS;

    public Field() {
        point2DS = new ArrayList<>();
    }

    public void setPoint2DS(List<Point2D> point2DS) {
        this.point2DS = point2DS;
    }

    public void addPoint2D(double x, double y) {
        point2DS.add(new Point2D(x, y));
    }

    public List<Point2D> getPoint2DS() {
        return point2DS;
    }

    public void clear() {
        point2DS.clear();
    }

    public void generate(int amount, int screenHeight, int screenWidth) {
        Random random = new Random();
        for (int i = 0; i < amount; i++) {
            point2DS.add(new Point2D(random.nextInt(screenWidth + 1), random.nextInt(screenHeight + 1)));
        }
    }

    public void exportData() throws IOException {
        String data = "";
        for (Point2D point2D : point2DS) {
            data = data +point2D.getX() + "," + point2D.getY()+"\n";
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("TravellingSalesmanExport" + System.currentTimeMillis()+".travel"));
        writer.write(data);
        writer.close();
    }

    public void importData(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            String[] data = currentLine.split(",");
            point2DS.add(new Point2D(Double.parseDouble(data[0]), Double.parseDouble(data[1])));
        }
        reader.close();
    }
}
