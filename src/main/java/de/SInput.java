package de;


import org.jscience.physics.amount.Amount;

import javax.measure.quantity.Acceleration;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Duration;
import javax.measure.quantity.Length;

/**
 * Created by Dinh-An on 23.02.2017.
 */
public class SInput {
    public Amount<Acceleration> acceleration;
    Amount<Angle> steering;
    public Amount<Length> x0;
    public Amount<Length> y0;
    public Amount<Duration> t0;
    public boolean doorStatus;
    public boolean indicatorStatus;
    public boolean lightTimerStatus;
    public boolean triggerStatus;

    public SInput(Amount<Acceleration> acceleration, Amount<Angle> steering, Amount<Length> x0,
                  Amount<Length> y0, Amount<Duration> t0,
                  boolean doorStatus, boolean indicatorStatus, boolean lightTimerStatus, boolean triggerStatus){
        this.acceleration = acceleration;
        this.steering = steering;
        this.x0 = x0;
        this.y0 = y0;
        this.t0 = t0;
        this.doorStatus = doorStatus;
        this.indicatorStatus = indicatorStatus;
        this.lightTimerStatus = lightTimerStatus;
        this.triggerStatus = triggerStatus;
    }
}
