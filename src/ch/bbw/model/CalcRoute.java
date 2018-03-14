package ch.bbw.model;

import ch.bbw.FXMLDocumentController;
import javafx.geometry.Point2D;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CalcRoute {
    double distance = 0;
    private ArrayList<Point2D> points;
    private boolean running = false, training;
    private FXMLDocumentController controller;

    public CalcRoute(FXMLDocumentController controller) {

        this.controller = controller;
        points = new ArrayList<>();
    }

    public void setTraining(boolean training) {
        this.training = training;
    }

    public void gpt(ArrayList<Point2D> points2d) {
        setGreedy(points2d);
        setTwoOptAnimation(points, 800);
    }

    public double acceptanceProbability(int energy, int newEnergy, double temperature) {
        if (newEnergy < energy) {
            return 1.0;
        }
        return Math.exp((energy - newEnergy) / temperature);
    }

    public boolean checkRunability() {
        if (running) {
            training = false;
            return false;
        } else {
            running = true;
            return true;
        }

    }


    public void setRandomPath(ArrayList<Point2D> points) {


        this.points = new ArrayList<>(points);
        Collections.shuffle(this.points, new Random());
        controller.finishProcess();
    }

    public void setGreedy(ArrayList<Point2D> points) {

        checkRunability();
        int startSize = points.size();
        this.points.clear();
        ArrayList<Point2D> unorderd = new ArrayList<>(points);
        Point2D actual = unorderd.get(0);
        this.points.add(actual);

        while (unorderd.size() != 1) {

            unorderd.remove(actual);
            Point2D nearest = unorderd.get(0);

            for (Point2D point2D : unorderd) {
                if (actual.distance(point2D) < actual.distance(nearest)) {
                    nearest = point2D;
                }
            }
            actual = nearest;
            synchronized (points) {
                this.points.add(actual);
            }
            controller.drawExtern();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

        controller.finishProcess();
    }

    public void setTwoOpt(ArrayList<Point2D> drawpoints) {


        checkRunability();
        points = new ArrayList<>(drawpoints);
        int size = points.size();

        ArrayList<Point2D> newRoute = new ArrayList<>(points);

        int improve = 0;
        int iteration = 0;

        while (improve < 800) {
            double bestdistance = getDistance();

            for (int i = 1; i < size - 1; i++) {
                for (int k = i + 1; k < size; k++) {
                    TwoOptSwap(i, k, newRoute);
                    iteration++;
                    double newdistance = getDistance(newRoute);

                    if (newdistance < bestdistance) {
                        improve = 0;

                        for (int j = 0; j < size; j++) {
                            points.set(j, newRoute.get(j));
                        }
                        bestdistance = newdistance;
                    }
                }
            }
            improve++;
        }
        controller.drawExtern();

        controller.finishProcess();
    }

    public void setTwoOptAnimation(ArrayList<Point2D> drawpoints, int iterations) {
        checkRunability();
        controller.setAnimationPlaying(true);
        points = new ArrayList<>(drawpoints);
        int size = points.size();

        ArrayList<Point2D> newRoute = new ArrayList<>(points);

        int improve = 0;
        int iteration = 0;

        while (improve < iterations) {
            double bestDistance = getDistance();

            for (int i = 1; i < size - 1; i++) {
                for (int k = i + 1; k < size; k++) {
                    TwoOptSwap(i, k, newRoute);
                    iteration++;
                    double newDistance = getDistance(newRoute);

                    if (newDistance < bestDistance) {
                        improve = 0;
                        synchronized (points) {
                            for (int j = 0; j < size; j++) {
                                points.set(j, newRoute.get(j));
                            }
                        }
                        bestDistance = newDistance;

                        controller.drawExtern();
                        try {
                            Thread.sleep(50
                            );
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            improve++;
        }

        controller.setAnimationPlaying(false);
        controller.finishProcess();
    }

    void TwoOptSwap(int i, int k, ArrayList<Point2D> newTour) {
        int size = points.size();
        synchronized (points) {
            for (int c = 0; c <= i - 1; ++c) {
                newTour.set(c, points.get(c));
            }
            int dec = 0;
            for (int c = i; c <= k; ++c) {
                newTour.set(c, points.get(k - dec));
                dec++;
            }
            for (int c = k + 1; c < size; ++c) {
                newTour.set(c, points.get(c));
            }
        }
    }

    public void geneticAlg(ArrayList<Point2D> unprocessed, int pop, boolean neighbour) {

        training = true;

        GA ga = new GA(unprocessed, pop, neighbour);
        while (training) {
            ga.iterate();
            synchronized (points) {
                points = ga.bestRoute.getPoints();
            }
            controller.drawExternGA(ga.getBestOfThisGen().getPoints());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public void simulatedAnnealing(ArrayList<Point2D> unprocessed) {

        checkRunability();
        controller.setAnimationPlaying(true);
        points = new ArrayList<>(unprocessed);

        double temp = unprocessed.size() * 10000;

        double coolingRate = 0.003;

        Route currentSolution = new Route(points);


        System.out.println("Initial solution distance: " + currentSolution.calcDistance());

        Route best = new Route(currentSolution.getPoints());
        best.calcDistance();

        while (temp > 1) {
            Route newSolution = new Route(currentSolution.getPoints());
            int tourPos1 = (int) (newSolution.getSize() * Math.random());
            int tourPos2 = (int) (newSolution.getSize() * Math.random());

            newSolution.swap(tourPos1, tourPos2);

            int currentEnergy = (int) currentSolution.calcDistance();
            int neighbourEnergy = (int) newSolution.calcDistance();

            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                currentSolution = new Route(newSolution.getPoints());
            }

            if (currentSolution.calcDistance() < best.calcDistance()) {
                best = new Route(currentSolution.getPoints());
            }

            temp *= 1 - coolingRate;
            synchronized (points) {
                points = best.getPoints();
            }
        }
        controller.drawExtern();

        System.out.println("Final solution distance: " + best.calcDistance());
        System.out.println("Tour: " + best);
        controller.setAnimationPlaying(false);
        controller.finishProcess();
    }


    public double getDistance() {

        double distance = 0;
        int count = 0;
        synchronized (points) {
            for (Point2D point2D : this.points) {
                count++;
                if (points.size() > count) {
                    distance += point2D.distance(points.get(count).getX(), points.get(count).getY());
                }
            }
            if (points.size() != 0) {
                distance += points.get(0).distance(points.get(points.size() - 1).getX(), points.get(points.size() - 1).getY());
            }
        }
        this.distance = distance;

        return this.distance;
    }


    public double getDistance(ArrayList<Point2D> points) {

        double distance = 0;
        int count = 0;
        for (Point2D point2D : points) {
            count++;
            if (points.size() > count) {
                distance += point2D.distance(points.get(count).getX(), points.get(count).getY());
            }
        }
        if (points.size() != 0) {
            distance += points.get(0).distance(points.get(points.size() - 1).getX(), points.get(points.size() - 1).getY());
        }
        this.distance = distance;
        return this.distance;
    }

    public ArrayList<Point2D> getPoints() {
        return points;
    }


    public BigInteger factorial(BigInteger number) {
        BigInteger result = BigInteger.valueOf(1);

        for (long factor = 2; factor <= number.longValue(); factor++) {
            result = result.multiply(BigInteger.valueOf(factor));
        }

        return result;
    }
}
