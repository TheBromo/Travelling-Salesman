package ch.bbw.model;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collections;

public class GARoute {
    ArrayList<Point2D> points;
    double fitness, disctance;

    public GARoute(ArrayList<Point2D> points) {
        this.points = new ArrayList<>(points);
        shuffle();
    }

    public GARoute(ArrayList<Point2D> points, double fitness) {
        this.points = points;
        this.fitness = fitness;
    }

    public GARoute(GARoute route) {
        this.points = new ArrayList<>(route.getPoints());
        this.fitness = route.getFitness();
        this.disctance = route.getDisctance();
    }

    public GARoute(double fitness) {
        this.fitness = fitness;
    }

    private void shuffle() {
        Collections.shuffle(points);
    }

    public void calculateFitness() {
        disctance = 0;

        int count = 0;
        for (Point2D point2D : points) {
            count++;
            if (points.size() > count) {
                disctance += point2D.distance(points.get(count).getX(), points.get(count).getY());
            }
        }
        fitness = 1 / (disctance + 1);
    }

    void swap(int indexA, int indexB) {
        Point2D temp = points.get(indexA);
        points.set(indexA, points.get(indexB));
        points.set(indexB, temp);

    }

    public void normalizeFitness(double totalFitness) {
        fitness = fitness / totalFitness;

    }

    public ArrayList<Point2D> getPoints() {
        return points;
    }

    public double getFitness() {

        return fitness;
    }

    public double initFitness(){
        calculateFitness();
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getDisctance() {
        return disctance;
    }
}
