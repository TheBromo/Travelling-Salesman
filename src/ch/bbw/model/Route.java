package ch.bbw.model;

import javafx.geometry.Point2D;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Route {
    private ArrayList<Point2D> points;

    public Route() {
        points = new ArrayList<>();
    }

    public void setRandomPath(ArrayList<Point2D> points) {
        Random random = new Random();
        this.points = new ArrayList<>(points);
        Collections.shuffle(this.points, new Random());
    }

    public void setGreedy(ArrayList<Point2D> points) {
        long start = System.nanoTime();
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
            this.points.add(actual);
        }
        this.points.add(unorderd.get(0));

        System.out.println(System.nanoTime() - start);

    }

    // Do all 2-opt combinations
    public void setTwoOpt(ArrayList<Point2D>drawpoints) {
        points = drawpoints;
        // Get tour size
        int size = points.size();

        //CHECK THIS!!

        ArrayList<Point2D> newRoute = new ArrayList<>(points);

        // repeat until no improvement is made
        int improve = 0;
        int iteration = 0;

        while (improve < 800) {
            double best_distance = getDistance();

            for (int i = 1; i < size - 1; i++) {
                for (int k = i + 1; k < size; k++) {
                    TwoOptSwap(i, k,newRoute);
                    iteration++;
                    double new_distance = getDistance(newRoute);

                    if (new_distance < best_distance) {
                        // Improvement found so reset
                        improve = 0;

                        for (int j = 0; j < size; j++) {
                            points.set(j, newRoute.get(j));
                        }

                        best_distance = new_distance;

                        // Update the display
                    }
                }
            }

            improve++;
        }
    }

    void TwoOptSwap(int i, int k, ArrayList<Point2D> newTour) {
        int size = points.size();

        // 1. take route[0] to route[i-1] and add them in order to new_route
        for (int c = 0; c <= i - 1; ++c) {
            newTour.set(c, points.get(c));
        }

        // 2. take route[i] to route[k] and add them in reverse order to new_route
        int dec = 0;
        for (int c = i; c <= k; ++c) {
            newTour.set(c, points.get(k - dec));
            dec++;
        }

        // 3. take route[k+1] to end and add them in order to new_route
        for (int c = k + 1; c < size; ++c) {
            newTour.set(c, points.get(c));
        }
    }


    public double getDistance() {

        double disctance = 0;
        int count = 0;
        for (Point2D point2D : points) {
            count++;
            if (points.size() > count) {
                disctance += point2D.distance(points.get(count).getX(), points.get(count).getY());
            }
        }
        disctance += points.get(0).distance(points.get(points.size() - 1).getX(), points.get(points.size() - 1).getY());
        return disctance;
    }


    public double getDistance(ArrayList<Point2D> points) {

        double disctance = 0;
        int count = 0;
        for (Point2D point2D : points) {
            count++;
            if (points.size() > count) {
                disctance += point2D.distance(points.get(count).getX(), points.get(count).getY());
            }
        }
        disctance += points.get(0).distance(points.get(points.size() - 1).getX(), points.get(points.size() - 1).getY());
        return disctance;
    }

    public ArrayList<Point2D> getPoints() {
        return points;
    }
}
