package de.rwth.visualization;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by Dinh-An on 08.06.2017.
 */
public class UpdateTest {

    @Test
    public void testXPos1(){
        // case no degree
        double x0 = 0;
        double degree = 0;
        double fpsTime = 1;
        double velocity = 5;

        double xi=x0+velocity*fpsTime*Math.cos(Math.toRadians(degree));
        assertTrue("New x position is wrong!",
                xi > 4.9 && xi < 5.1);
    }

    @Test
    public void testXPos2(){
        // case no velocity
        double x0 = 50;
        double degree = 20;
        double fpsTime = 1;
        double velocity = 0;

        double xi=x0+velocity*fpsTime*Math.cos(Math.toRadians(degree));
        assertTrue("New x position is wrong!",
                xi > 49 && xi < 51);
    }

    @Test
    public void testXPos3(){
        // case negative global degree
        double x0 = 100;
        double degree = -40;
        double fpsTime = 1;
        double velocity = 20;

        double xi=x0+velocity*fpsTime*Math.cos(Math.toRadians(degree));
        assertTrue("New x position is wrong!",
                xi > 115 && xi < 115.5);
    }

    @Test
    public void testXPos4(){
        // case
        double x0 = 150;
        double degree = 90;
        double fpsTime = 1;
        double velocity = 100;

        double xi=x0+velocity*fpsTime*Math.cos(Math.toRadians(degree));
        assertTrue("New x position is wrong!",
                xi > 149 && xi < 151);
    }

    @Test
    public void testYPos1(){
        // case
        double y0 = 50;
        double degree = 20;
        double fpsTime = 1;
        double velocity = 0;

        double yi=y0+velocity*fpsTime*Math.sin(Math.toRadians(degree));
        assertTrue("New y position is wrong!",
                yi > 49 && yi < 51);
    }

    @Test
    public void testYPos2(){
        // case
        double y0 = 100;
        double degree = -40;
        double fpsTime = 1;
        double velocity = 20;

        double yi=y0+velocity*fpsTime*Math.sin(Math.toRadians(degree));
        assertTrue("New y position is wrong!",
                yi > 87 && yi < 87.5);
    }

    @Test
    public void testYPos3(){
        // case
        double y0 = 150;
        double degree = 90;
        double fpsTime = 1;
        double velocity = 100;

        double yi=y0+velocity*fpsTime*Math.sin(Math.toRadians(degree));
        assertTrue("New y position is wrong!",
                yi > 249.9 && yi < 250.1);
    }

}
