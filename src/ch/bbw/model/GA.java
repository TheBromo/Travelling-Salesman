package ch.bbw.model;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Random;

public class GA {
    ArrayList<GARoute> population;
    double totalFitness;
    GARoute bestRoute;
    boolean neigh = false;

    public GA(ArrayList<Point2D> points) {

        totalFitness = 0;
        population = new ArrayList<>();

        for (int i = 0; i < 300; i++) {
            population.add(new GARoute(points));
            population.get(i).shuffle();
            totalFitness += population.get(i).initFitness();
        }
        for (GARoute route : population) {
            route.normalizeFitness(totalFitness);
        }
        bestRoute = population.get(0);
    }

    public GA(ArrayList<Point2D> points, int pop, boolean neigh) {

        totalFitness = 0;
        population = new ArrayList<>();

        for (int i = 0; i < pop; i++) {
            population.add(new GARoute(points));
            population.get(i).shuffle();
            totalFitness += population.get(i).initFitness();
        }
        for (GARoute route : population) {
            route.normalizeFitness(totalFitness);
        }
        this.neigh = neigh;
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
            GARoute list1 = pickOne();
            GARoute list2 = pickOne();
            ArrayList<Point2D> newList = crossover(list1.getPoints(), list2.getPoints());
            newGen.add(new GARoute(newList));
            mutate(newGen.get(i), 0.01);
        }
        population = new ArrayList<>(newGen);
    }

    private void mutate(GARoute route, double mutationRate) {
        Random random = new Random();
        for (int i = 0; i < route.getPoints().size(); i++) {
            if (random.nextDouble() < mutationRate) {
                int indexA = (int) Math.floor(random.nextInt(route.getPoints().size()));
                int indexB;
                if (neigh) {
                    indexB = (indexA + 1) % route.getPoints().size();
                } else {
                    indexB = (int) Math.floor(random.nextInt(route.getPoints().size()));

                }
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
        if (Math.random() > 0.99) {
            return new GARoute(bestRoute.getPoints(), bestRoute.getFitness());
        }
        return new GARoute(population.get(index).getPoints(), population.get(index).getFitness());
    }


    private void updateFitness() {
        totalFitness = 0;
        for (GARoute route : population) {
            route.calculateFitness();
            totalFitness += route.getFitness();
        }
    }

    private void normalizeFitness() {
        for (GARoute route : population) {
            route.normalizeFitness(totalFitness);
        }
    }

    private ArrayList<Point2D> crossover(ArrayList<Point2D> list1, ArrayList<Point2D> list2) {
        ArrayList<Point2D> newList = new ArrayList<>();
        int start = new Random().nextInt(list1.size());
        int end = 1 + start + new Random().nextInt(list1.size() - start);


        for (int i = start; i < end; i++) {
            newList.add(list1.get(i));
        }
        int left = list1.size() - newList.size();
        for (Point2D point2D : list2) {
            if (!newList.contains(point2D)) {
                newList.add(point2D);
            }
        }
        return newList;

    }

}
