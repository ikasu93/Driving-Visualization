package de;

import org.jscience.physics.amount.Amount;

import javax.measure.quantity.Angle;
import javax.measure.quantity.Duration;
import javax.measure.quantity.Length;
import javax.measure.quantity.Velocity;

/**
 * Created by Dinh-An on 23.02.2017.
 */
public class SOutput {
    public Amount<Velocity> velocity;
    public Amount<Length> xi;
    public Amount<Length> yi;
    public Amount<Duration> ti;
    public Amount<Angle> degree;
    /*
    // public Amount<Length> distanceFrontRight;
    // public Amount<Length> distanceFrontLeft;
    // public Amount<Length> distanceFrontRightSide;
    // public Amount<Length> distanceFrontLeftSide;
    // public Amount<Length> distanceBackRight;
    // public Amount<Length> distanceBackLeft;
    // public Amount<Length> distanceBackRightSide;
    // public Amount<Length> distanceBackLeftSide;
     */
    public boolean doorStatus;
    public boolean indicatorStatus;
    public boolean lightTimerStatus;
    public boolean triggerStatus;

    public SOutput(Amount<Velocity> velocity, Amount<Length> xi, Amount<Length> yi,
                   Amount<javax.measure.quantity.Duration> ti, Amount<Angle> degree,
                   boolean doorStatus, boolean indicatorStatus, boolean lightTimerStatus, boolean triggerStatus){
        this.velocity = velocity;
        this.xi = xi;
        this.yi = yi;
        this.ti = ti;
        this.degree = degree;

        this.doorStatus = doorStatus;
        this.indicatorStatus = indicatorStatus;
        this.lightTimerStatus = lightTimerStatus;
        this.triggerStatus = triggerStatus;
    }
}
