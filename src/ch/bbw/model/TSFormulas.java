package ch.bbw.model;

import javafx.geometry.Point2D;

import java.util.*;

public class TSFormulas {
    private ArrayList<Point2D> points;

    public TSFormulas() {
        points = new ArrayList<>();
    }

    public void setRandompath(ArrayList<Point2D> points) {
        Random random = new Random();
        this.points = new ArrayList<>(points);
        Collections.shuffle(this.points, new Random());
    }

    public double getDisctance() {
        double disctance = 0;
        int count = 0;
        for (Point2D point2D : points) {
            count++;
            if (points.size()>count) {
                disctance += point2D.distance(points.get(count).getX(), points.get(count).getY());
            }
        }
        return disctance;
    }

    public ArrayList<Point2D> getPoints() {
        return points;
    }
}
