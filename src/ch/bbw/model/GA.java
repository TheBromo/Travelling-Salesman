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
            population.get(i).shuffle();
            totalfitness += population.get(i).initFitness();
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
    }

    public GARoute getBestRoute() {
        return bestRoute;
    }

    private void setBestRoute() {

        for (GARoute route : population) {
            if (route.calcDistance() < bestRoute.getDistance()) {
                bestRoute = new GARoute(route);
            }
        }
    }

    GARoute getBestOfThisGen() {
        GARoute best = population.get(0);
        for (GARoute route : population) {
            if (route.calcDistance() < best.calcDistance()) {
                best = new GARoute(route);
            }
        }
        return best;
    }

    private void nextGeneration() {
        ArrayList<GARoute> newGen = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            newGen.add(pickOne());
            mutate(newGen.get(i), 0.01);
        }
        population = new ArrayList<>(newGen);
    }

    private void mutate(GARoute route, double mutationRate) {
        Random random = new Random();
        for (int i = 0; i < route.getPoints().size(); i++) {
            if (random.nextDouble() < mutationRate) {
                int indexA = (int) Math.floor(random.nextInt(route.getPoints().size()));
                int indexB = (int) Math.floor(random.nextInt(route.getPoints().size()));
                route.swap(indexA, indexB);
            }
        }
    }


    private GARoute pickOne() {
        int index = 0;
        double r = Math.random();
        while (r > 0) {
            r = r - population.get(index).getFitness();
            index++;
        }
        index--;
        if (Math.random()>0.99){
            return new GARoute(bestRoute.getPoints(), bestRoute.getFitness());
        }
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
