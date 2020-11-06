package de.rwth.visualization.track;

import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Dinh-An on 31.05.2017.
 */
public abstract class Wall {
    public boolean inBoundaries(RealVector pointLeft, RealVector point, RealVector pointRight) {
        double xPointLeft = pointLeft.getEntry(1);
        double xPointRight = pointRight.getEntry(1);
        double xPoint = point.getEntry(1);

        double yPointLeft = pointLeft.getEntry(0);
        double yPointRight = pointRight.getEntry(0);
        double yPoint = point.getEntry(0);

        double xMin = Math.min(xPointLeft, xPointRight) - 1.1;
        double xMax = Math.max(xPointLeft, xPointRight) + 1.1;

        double yMin = Math.min(yPointLeft, yPointRight) - 1.1;
        double yMax = Math.max(yPointLeft, yPointRight) + 1.1;

        /*if(yMin == yMax){
            yMin-=0.01;
            yMax+=0.01;
        }
        if(xMin == xMax){
            xMin-=0.01;
            xMax+=0.01;
        }*/

        boolean inHeight = yMin <= yPoint && yPoint <= yMax;
        boolean inWidth = xMin <= xPoint && xPoint <= xMax;

        return inHeight && inWidth;
    }
}
