package de.rwth.visualization;

import de.rwth.visualization.car.Car;
import de.rwth.visualization.coord.Orientation;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by Dinh-An on 01.06.2017.
 */
public class DistanceTest {
    @Test
    public void testFLDistances1() {
        Car.setDegree(0);
        Car.setPosition(0, 0);

        double distance =
                Car.getSensor(Orientation.FRONT_LEFT).getMinDistance();
        assertTrue("FRONT_LEFT Distance is wrong!",
                distance > 70 && distance < 71);
    }

    @Test
    public void testFLDistances2() {
        Car.setDegree(45);
        Car.setPosition(10, 0);

        double distance =
                Car.getSensor(Orientation.FRONT_LEFT).getMinDistance();
        assertTrue("FRONT_LEFT Distance is wrong!",
                distance > 11 && distance < 11.2);
    }

    @Test
    public void testFLDistances3() {
        // case at left circle with degree 0
        Car.setDegree(0);
        Car.setPosition(170, 0);

        double distance =
                Car.getSensor(Orientation.FRONT_LEFT).getMinDistance();
        assertTrue("FRONT_LEFT Distance is wrong!",
                distance > 2 && distance < 8);
    }

    @Test
    public void testFLDistances4() {
        // case at left circle with degree -170
        Car.setDegree(-60);
        Car.setPosition(170, 0);

        double distance =
                Car.getSensor(Orientation.FRONT_LEFT).getMinDistance();
        assertTrue("FRONT_LEFT Distance is wrong!",
                distance > 24 && distance < 25);
    }

    @Test
    public void testFRDistances1() {
        // case at left circle with degree 60
        Car.setDegree(-60);
        Car.setPosition(170, 0);

        double distance =
                Car.getSensor(Orientation.FRONT_RIGHT).getMinDistance();
        assertTrue("FRONT_RIGHT Distance is wrong!",
                distance > 18 && distance < 24);
    }

    @Test
    public void testDistances2() {
        double x = 5;
        double y = 58;

        Car.setPosition(x, y);

        double distance =
                Car.getSensor(Orientation.FRONT_LEFT).getMinDistance();
        assertTrue("FRONT_LEFT Distance is wrong!",
                distance > 2.14 && distance < 2.16);

        distance =
                Car.getSensor(Orientation.FRONT_LEFT_SIDE).getMinDistance();
        assertTrue("FRONT_LEFT_SIDE Distance is wrong!",
                distance > 1.074 && distance < 1.076);

        distance =
                Car.getSensor(Orientation.BACK_LEFT_SIDE).getMinDistance();
        assertTrue("BACK_LEFT_SIDE Distance is wrong!",
                distance > 3.14 && distance < 3.16);

        distance =
                Car.getSensor(Orientation.FRONT_RIGHT).getMinDistance();
        assertTrue("FRONT_RIGHT Distance is wrong!",
                distance > 9.5 && distance < 9.6);

        Car.setDegree(-90);
        Car.setPosition(0, 170);

        distance =
                Car.getSensor(Orientation.FRONT_LEFT).getMinDistance();
        assertTrue("FRONT_LEFT Distance is wrong!",
                distance > 0 && distance < 1000);

        distance =
                Car.getSensor(Orientation.FRONT_LEFT_SIDE).getMinDistance();
        assertTrue("FRONT_LEFT_SIDE Distance is wrong!",
                distance > 0 && distance < 1000);

        distance =
                Car.getSensor(Orientation.BACK_LEFT_SIDE).getMinDistance();
        assertTrue("BACK_LEFT_SIDE Distance is wrong!",
                distance > 0 && distance < 1000);

        distance =
                Car.getSensor(Orientation.FRONT_RIGHT).getMinDistance();
        assertTrue("FRONT_RIGHT Distance is wrong!",
                distance > 0 && distance < 1000);
    }
}