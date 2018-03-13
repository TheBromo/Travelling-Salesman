package ch.bbw.model;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collections;

public class Route {

    ArrayList<Point2D> points;
    double distance;

    public Route(ArrayList<Point2D> points) {
        this.points = new ArrayList<>(points);


    }

    public Route() {
    }

     void shuffle() {
        Collections.shuffle(points);
        distance =calcDistance();
    }

    void swap(int indexA, int indexB) {
        Point2D temp = points.get(indexA);
        points.set(indexA, points.get(indexB));
        points.set(indexB, temp);

    }

    double calcDistance() {
        distance = 0;
        int count = 0;
        for (Point2D point2D : points) {
            count++;
            if (points.size() > count) {
                distance += point2D.distance(points.get(count).getX(), points.get(count).getY());
            }
        }

        distance += points.get(0).distance(points.get(points.size() - 1).getX(), points.get(points.size() - 1).getY());
        return distance;
    }

    public double getDistance() {
        return distance;
    }

    public ArrayList<Point2D> getPoints() {
        return points;
    }

    int getSize(){
        return points.size();
    }


}
