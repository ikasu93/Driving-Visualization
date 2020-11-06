package de.rwth.visualization;

import de.rwth.visualization.car.Car;
import de.rwth.visualization.coord.Orientation;
import de.rwth.visualization.track.Track;
import de.rwth.visualization.track.Wall;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class SensorTest {
    @Test
    public void testGetIntersection5() {
        Car.setPosition(170, 0);
        Car.setDegree(-60);

        Wall wall5 = Track.walls.get(4);
        List<RealVector> intersections =
                Car.getSensor(Orientation.FRONT_RIGHT).getIntersections(wall5);
        Assert.assertTrue("Sensor does not intersect with Wall 5!", intersections.size() == 2);
    }

    @Test
    public void testGetIntersection8() {
        Car.setPosition(63, -3);
        Car.setDegree(-40);

        Wall wall8 = Track.walls.get(7);
        List<RealVector> intersections =
                Car.getSensor(Orientation.FRONT_RIGHT).getIntersections(wall8);
        Assert.assertTrue("Sensor does not intersect with Wall 8!", intersections.size() == 2);
    }

    @Test
    public void testGetParameters8() {
        Car.setPosition(63, -3);
        Car.setDegree(-40);

        Wall wall8 = Track.walls.get(7);
        List<Double> parameters =
                Car.getSensor(Orientation.FRONT_RIGHT).getParameters(wall8);
        Assert.assertTrue("Sensor does not intersect with Wall 8!", parameters.size() == 2);
    }

    @Test
    public void testGetAllDistances() {
        Car.setPosition(63, -3);
        Car.setDegree(-40);

        List<Double> distances =
                Car.getSensor(Orientation.FRONT_RIGHT).getAllDistances();
        Assert.assertTrue("Sensor does not intersect with Wall 8!", distances.size() == 3);
    }

    @Test
    public void testGetDirection() {
        Car.setPosition(0, 0);
        Car.setDegree(90);

        RealVector direction =
                Car.getSensor(Orientation.FRONT_LEFT).getDirection();
        double y = direction.getEntry(1);
        double x = direction.getEntry(0);

        Assert.assertTrue("y direction is wrong!", -1.5<y&& y<-0.5);
        Assert.assertTrue("x direction is wrong!", -0.5<x && x<0.5);
    }

    @Test
    public void testGetPositionFL() {
        Car.setPosition(0, 0);
        Car.setDegree(90);

        RealVector pos = Car.getSensor(Orientation.FRONT_LEFT).getPosition();

        Assert.assertTrue("y pos is wrong!", -5<pos.getEntry(1)&& pos.getEntry(1)<-4);
        Assert.assertTrue("x pos is wrong!", 1<pos.getEntry(0) && pos.getEntry(0)<3);
    }

    @Test
    public void testGetPositionFR() {
        Car.setPosition(0, 0);
        Car.setDegree(90);

        RealVector pos = Car.getSensor(Orientation.FRONT_RIGHT).getPosition();

        Assert.assertTrue("y pos is wrong!", -5<pos.getEntry(1)&& pos.getEntry(1)<-4);
        Assert.assertTrue("x pos is wrong!", -1<pos.getEntry(0) && pos.getEntry(0)<-3);
    }

    @Test
    public void testGetPositionSRF() {
        Car.setPosition(0, 0);
        Car.setDegree(90);

        RealVector pos = Car.getSensor(Orientation.FRONT_RIGHT_SIDE).getPosition();

        Assert.assertTrue("y pos is wrong!", 4<pos.getEntry(1)&& pos.getEntry(1)<5);
        Assert.assertTrue("x pos is wrong!", -1<pos.getEntry(0) && pos.getEntry(0)<-3);
    }

    @Test
    public void testGetPositionSRF2() {
        Car.setPosition(70, -4);
        Car.setDegree(60);

        RealVector pos = Car.getSensor(Orientation.FRONT_RIGHT_SIDE).getPosition();

        Assert.assertTrue("y pos is wrong!", 4<pos.getEntry(1)&& pos.getEntry(1)<5);
        Assert.assertTrue("x pos is wrong!", -1<pos.getEntry(0) && pos.getEntry(0)<-3);
    }

}