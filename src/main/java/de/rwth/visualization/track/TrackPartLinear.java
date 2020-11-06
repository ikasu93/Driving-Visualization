package de.rwth.visualization.track;

import org.apache.commons.math3.linear.RealVector;

public class TrackPartLinear extends TrackPart {
    public final RealVector pointUpperLeft;
    public final RealVector pointUpperRight;
    public final RealVector pointLowerLeft;
    public final RealVector pointLowerRight;

    public TrackPartLinear(RealVector pointUpperLeft, RealVector pointUpperRight,
                           RealVector pointLowerLeft, RealVector pointLowerRight) {
        this.pointUpperLeft = pointUpperLeft;
        this.pointUpperRight = pointUpperRight;
        this.pointLowerLeft = pointLowerLeft;
        this.pointLowerRight = pointLowerRight;
    }
}