package ch.bbw.model;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Random;

public class GA {
    ArrayList<GARoute> population;
    double totalfitness;
    GARoute bestRoute;

    public GA(ArrayList<Point2D> points) {

        totalfitness = 0;
        population = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            population.add(new GARoute(points));
            totalfitness += population.get(0).getFitness();
        }
        for (GARoute route : population) {
            route.normalizeFitness(totalfitness);
        }
        bestRoute = population.get(0);
    }

    public void iterate() {
        updateFitness();
        normalizeFitness();
        setBestRoute();
        nextGeneration();
        System.out.println(bestRoute.disctance);
    }

    public GARoute getBestRoute() {
        return bestRoute;
    }

    private void setBestRoute() {
        System.out.println("##########################################\n Population: ");
        for (GARoute route : population) {
            System.out.println("Distance: " + route.disctance + " Fitness: " + route.getFitness());
        }
        for (GARoute route : population) {
            if (route.getDisctance() < bestRoute.getDisctance()) {
                bestRoute = route;
            }
        }
    }

    private void nextGeneration() {
        ArrayList<GARoute> newGen = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            newGen.add(pickOne());
            mutate(newGen.get(i), 7);
        }

        population = new ArrayList<>(newGen);

    }

    private void mutate(GARoute route, double mutationRate) {
        Random random = new Random();
        for (int i = 0; i < mutationRate; i++) {
            int indexA = (int) Math.floor(random.nextInt(route.getPoints().size()));
            int indexB = (int) Math.floor(random.nextInt(route.getPoints().size()));
            route.swap(indexA, indexB);
        }


    }


    private GARoute pickOne() {
        int index = 0;
        double r = new Random().nextDouble();
        while (r > 0) {
            r = r - population.get(index).getFitness();
            index++;
        }
        index--;
        return new GARoute(population.get(index).getPoints(), population.get(index).getFitness());
    }


    private void updateFitness() {
        totalfitness = 0;
        for (GARoute route : population) {
            route.calculateFitness();
            totalfitness += route.getFitness();
        }
    }

    private void normalizeFitness() {
        for (GARoute route : population) {
            route.normalizeFitness(totalfitness);
        }
    }
}
