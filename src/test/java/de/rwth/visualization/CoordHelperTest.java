package de.rwth.visualization;

import de.CoordHelper;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class CoordHelperTest {
    @Test
    public void testGetIntersectionLine() {
        RealVector p1 = new ArrayRealVector(2);

        p1.setEntry(0, -9.5);
        p1.setEntry(1, 58);

        RealVector p2 = new ArrayRealVector(2);

        p2.setEntry(0, -9.5);
        p2.setEntry(1, -122);

        RealVector s = new ArrayRealVector(2);

        s.setEntry(0, 6.85);
        s.setEntry(1, 62.15);

        RealVector d = new ArrayRealVector(2);

        d.setEntry(0, 1);
        d.setEntry(1, 0);

        RealVector intersection =
                CoordHelper.getIntersectionLine(p1, p2, s, d);

        Assert.assertTrue("Wrong Intersection Point! - X",
                intersection.getEntry(0) > -9.6 && intersection.getEntry(0) < -9.4);
        Assert.assertTrue("Wrong Intersection Point! - Y",
                intersection.getEntry(1) > 59 && intersection.getEntry(1) < 63);
    }

    @Test
    public void testIntersectionWithLine(){
        RealVector p1 = new ArrayRealVector(2);
        p1.setEntry(0, -9.5);
        p1.setEntry(1, 58);

        RealVector p2 = new ArrayRealVector(2);
        p2.setEntry(0, -9.5);
        p2.setEntry(1, 58);

        RealVector s = new ArrayRealVector(2);
        s.setEntry(0, -9.5);
        s.setEntry(1, 58);

        RealVector d = new ArrayRealVector(2);
        d.setEntry(0, -9.5);
        d.setEntry(1, 58);


        double scalar = (p1.getEntry(0)*(p2.getEntry(1)-p1.getEntry(1))-p1.getEntry(1)*(p2.getEntry(0)-p1.getEntry(0))
                -s.getEntry(0)*(p2.getEntry(1)-p1.getEntry(1))+s.getEntry(1)*(p2.getEntry(0)-p1.getEntry(0)))/
                (d.getEntry(0)*(p2.getEntry(1)-p1.getEntry(1))-d.getEntry(1)*(p2.getEntry(0)-p1.getEntry(0)));
        RealVector intersection = s.add(d.mapMultiply(scalar));

        Assert.assertTrue("Wrong Intersection Point! - X",
                intersection.getEntry(0) < 10&& intersection.getEntry(0) < 100);
    }

    @Test
    public void testIntersectionWithCircle1() {
        RealVector sensor = new ArrayRealVector(2);
        sensor.setEntry(0, 173.677146997);
        sensor.setEntry(1, 2.6690054257);

        RealVector direction = new ArrayRealVector(2);
        direction.setEntry(0,0.5);
        direction.setEntry(1,0.8660254038);

        RealVector middlePoint = new ArrayRealVector(2);
        middlePoint.setEntry(1, 22);
        middlePoint.setEntry(0, 123);

        double r = 60.0;
        boolean plus = false;

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

        System.out.println("scalar: "+scalar);
        RealVector intersection = sensor.add(direction.mapMultiply(scalar));
        System.out.println("x-Value: "+intersection.getEntry(1));
        System.out.println("y-Value: "+intersection.getEntry(0));

        Assert.assertTrue("Wrong Intersection Point! - X",
                intersection.getEntry(0) < 184&& intersection.getEntry(0) < 183);
    }

    @Test
    public void testIntersectionWithCircle2() {
        RealVector sensor = new ArrayRealVector(2);
        sensor.setEntry(0, 173.677146997);
        sensor.setEntry(1, 2.6690054257);

        RealVector direction = new ArrayRealVector(2);
        direction.setEntry(0,0.5);
        direction.setEntry(1,0.8660254038);

        RealVector middlePoint = new ArrayRealVector(2);
        middlePoint.setEntry(1, 22);
        middlePoint.setEntry(0, 123);

        double r = 60.0;
        boolean plus = true;

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

        System.out.println("scalar: "+scalar);
        RealVector intersection = sensor.add(direction.mapMultiply(scalar));
        System.out.println("x-Value: "+intersection.getEntry(1));
        System.out.println("y-Value: "+intersection.getEntry(0));

        Assert.assertTrue("Wrong Intersection Point! - X",
                intersection.getEntry(0) < 184&& intersection.getEntry(0) < 183);
    }

    @Test
    public void testScalar1() {
        RealVector intersection = new ArrayRealVector(2);
        intersection.setEntry(0, 182.9);
        intersection.setEntry(1, 18.655);

        RealVector position = new ArrayRealVector(2);
        position.setEntry(0, 173.677146997);
        position.setEntry(1, 2.6690054257);

        RealVector direction = new ArrayRealVector(2);
        direction.setEntry(0, 0.5);
        direction.setEntry(1, 0.8660254038);

        double scalar = direction.getEntry(0) == 0.0 ?
                (intersection.getEntry(1) - position.getEntry(1)) / direction.getEntry(1) :
                (intersection.getEntry(0) - position.getEntry(0)) / direction.getEntry(0);
        System.out.println("Scalar: " + scalar);

        Assert.assertTrue("Wrong Intersection Point! - X",
                scalar > 17 && scalar < 19);
    }
}