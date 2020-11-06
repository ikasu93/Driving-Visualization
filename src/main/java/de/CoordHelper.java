package de;

import com.sun.org.apache.regexp.internal.RE;
import javafx.geometry.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Dinh-An on 12.05.2017.
 */
public class CoordHelper {
    public static RealVector getSensorBL(RealVector carPosition) {
        RealVector offset = new ArrayRealVector();

        offset.setEntry(0, 1.85);
        offset.setEntry(1, -4.15);

        return carPosition.add(offset);
    }

    public static RealVector getSensorFL(RealVector carPosition) {
        RealVector offset = new ArrayRealVector();

        offset.setEntry(0, 1.85);
        offset.setEntry(1, 4.15);

        return carPosition.add(offset);
    }

    public static RealVector getSensorFR(RealVector carPosition) {
        RealVector offset = new ArrayRealVector();

        offset.setEntry(0, -1.85);
        offset.setEntry(1, 4.15);

        return carPosition.add(offset);
    }

    public static RealVector getSensorBR(RealVector carPosition) {
        RealVector offset = new ArrayRealVector();

        offset.setEntry(0, -1.85);
        offset.setEntry(1, -4.15);

        return carPosition.add(offset);
    }

    public static double f(double x) {
        return 0.0;
    }

    /*public static Vector<Double> getDistance1(double x1, double y1, double x2, double y2,
                                              double cx, double cy) {
                                              \\TODO
    }*/

    /*public static Vector<Double> getDirectionFR(double cx, double cy, double degree, double lambda) {
        Vector<Double> s3 = CoordHelper.getSensorFR(cx, cy);

        double radians = Math.toRadians(degree);
        double x = s3.get(0) - lambda * Math.sin(radians);
        double y = s3.get(1) + lambda * Math.cos(radians);

        Vector<Double> vector = new Vector<>();

        vector.add(x);
        vector.add(y);

        return vector;
    }*/


    //Intersection between sensor and linear function
    public static RealVector getIntersectionLine(RealVector p1, RealVector p2, RealVector s, RealVector d) {
        double scalar = (p1.getEntry(0)*(p2.getEntry(1)-p1.getEntry(1))-p1.getEntry(1)*(p2.getEntry(0)-p1.getEntry(0))
                -s.getEntry(0)*(p2.getEntry(1)-p1.getEntry(1))+s.getEntry(1)*(p2.getEntry(0)-p1.getEntry(0)))/
                (d.getEntry(0)*(p2.getEntry(1)-p1.getEntry(1))-d.getEntry(1)*(p2.getEntry(0)-p1.getEntry(0)));
        RealVector intersection = s.add(d.mapMultiply(scalar));

        return intersection;
    }

    //Distance from sensor to intersection
    public static double getDistanceLine(RealVector p1, RealVector p2, RealVector s, RealVector r) {
        try {
            RealVector intersection = CoordHelper.getIntersectionLine(p1, p2, s, r);
            return intersection.getDistance(s);
        } catch(Exception e) {
            return Double.MAX_VALUE;
        }
    }

    public static RealVector getIntersectionCircle(RealVector sensor, RealVector direction, RealVector middlePoint,
                                                   double r, boolean plus) {
        double plusValue = plus ? 1.0 : -1.0;

        double a = direction.getEntry(0);
        double b = direction.getEntry(1);

        double m = middlePoint.getEntry(0);
        double n = middlePoint.getEntry(1);

        double x = sensor.getEntry(0);
        double y = sensor.getEntry(1);

        double as = Math.pow(a,2)+Math.pow(b,2);
        double xm = Math.pow(x-m,2)+Math.pow(y-n,2)-Math.pow(r,2);
        double za = Math.pow(2*a*(x-m)+2*b*(y-n), 2);
        double zaq = 2*Math.pow(a,2)+2*Math.pow(b,2);
        double sqrtValue = -4*as*xm+za;
        if(sqrtValue<0 && sqrtValue>= -0.0001){
            sqrtValue = 0;
        }
        double sqrt = plusValue*Math.sqrt(sqrtValue);
        double scalar = -1*(sqrt +2*a*(x-m)+2*b*(y-n))/zaq;

        RealVector intersection = sensor.add(direction.mapMultiply(scalar));
        return intersection;
    }

    public static List<RealVector> getIntersectionCircle(RealVector s, RealVector d, RealVector m,
                                                  double radius) {
        List<RealVector> result = new ArrayList<>();

        RealVector pointUpper = CoordHelper.getIntersectionCircle(s, d, m, radius, true);
        RealVector pointLower = CoordHelper.getIntersectionCircle(s, d, m, radius, false);

        result.add(pointUpper);
        result.add(pointLower);

        return result;
    }

    public static double getDistanceCircle(RealVector s, RealVector d, RealVector m, double radius, boolean plus) {
        try {
            RealVector intersection = CoordHelper.getIntersectionCircle(s, d, m, radius, plus);
            return intersection.getDistance(s);
        } catch (Exception e) {
            return Double.MAX_VALUE;
        }
    }

}