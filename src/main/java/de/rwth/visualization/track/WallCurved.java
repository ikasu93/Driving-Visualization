package de.rwth.visualization.track;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Dinh-An on 31.05.2017.
 */
public class WallCurved extends Wall {
    public static Builder getBuilder() {
        return new Builder();
    }


    public final RealVector pointMiddle;
    public final double radius;
    public final RealVector pointLower;
    public final RealVector pointUpper;

    public WallCurved(RealVector pointMiddle, double radius, RealVector pointLower, RealVector pointUpper) {
        this.pointMiddle = pointMiddle;
        this.radius = radius;
        this.pointLower = pointLower;
        this.pointUpper = pointUpper;
    }

    public boolean inBoundaries(RealVector point) {
        return super.inBoundaries(this.pointLower, point, this.pointUpper);
    }


    public static class Builder {
        private RealVector pointMiddle;
        private double radius;
        private RealVector pointLower;
        private RealVector pointUpper;

        Builder() {
            this.pointMiddle = new ArrayRealVector(2);
            this.pointLower = new ArrayRealVector(2);
            this.pointUpper = new ArrayRealVector(2);
        }

        public Builder setPointMiddle(double x, double y) {
            this.pointMiddle.setEntry(0, x);
            this.pointMiddle.setEntry(1, y);
            return this;
        }

        public Builder setPointMiddle(RealVector pointMiddle) {
            this.pointMiddle = pointMiddle;
            return this;
        }

        public Builder setRadius(double radius) {
            this.radius = radius;
            return this;
        }

        public Builder setPointUpper(double x, double y) {
            this.pointUpper.setEntry(0, x);
            this.pointUpper.setEntry(1, y);
            return this;
        }

        public Builder setPointUpper(RealVector pointUpper) {
            this.pointUpper = pointUpper;
            return this;
        }

        public Builder setPointLower(double x, double y) {
            this.pointLower.setEntry(0, x);
            this.pointLower.setEntry(1, y);
            return this;
        }

        public Builder setPointLower(RealVector pointLower) {
            this.pointLower = pointLower;
            return this;
        }

        public WallCurved build() {
            return new WallCurved(this.pointMiddle, this.radius, this.pointLower,
                    this.pointUpper);
        }
    }
}