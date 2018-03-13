package ch.bbw.model;

import javafx.geometry.Point2D;

import java.util.ArrayList;

public class GARoute extends Route {
    double fitness;


    public GARoute(ArrayList<Point2D> points, double fitness) {
        super(points);
        this.fitness = fitness;
        distance = calcDistance();
    }

    public GARoute(GARoute route) {
        super(route.getPoints());
        this.distance = calcDistance();
        this.fitness = route.getFitness();

    }

    public GARoute(ArrayList<Point2D> points) {
        super(points);
        distance = calcDistance();
    }

    public GARoute(double fitness) {
        this.fitness = fitness;
    }


    public void calculateFitness() {
        distance = calcDistance();
        fitness = 1 / (distance + 1);
    }

    public void normalizeFitness(double totalFitness) {
        fitness = fitness / totalFitness;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double initFitness() {
        calculateFitness();
        return fitness;
    }


}
