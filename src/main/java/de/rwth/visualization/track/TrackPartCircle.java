package de.rwth.visualization.track;

import org.apache.commons.math3.linear.RealVector;

public class TrackPartCircle extends TrackPart {
    public final RealVector point;
    public final double radiusInner;
    public final double radiusOuter;

    public TrackPartCircle(RealVector point, double radiusInner, double radiusOuter) {
        this.point = point;
        this.radiusInner = radiusInner;
        this.radiusOuter = radiusOuter;
    }
}