package ch.bbw.model;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TSFormulas {
    private List<Point2D>points;

    public TSFormulas(){
        points = new ArrayList<>();
    }

    public void setRandompath(ArrayList<Point2D> points){
        Random random =new Random();
        ArrayList<Point2D>pointTemps = (ArrayList<Point2D>) points.clone();
        while (pointTemps.size()!=0){
            int index=random.nextInt(pointTemps.size());
            points.add(pointTemps.get(index));
            points.remove(index);
            points.trimToSize();
        }

    }
}
