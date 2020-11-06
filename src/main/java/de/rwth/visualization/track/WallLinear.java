package de.rwth.visualization.track;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by Dinh-An on 31.05.2017.
 */
public class WallLinear extends Wall {
    public static Builder getBuilder() {
        return new Builder();
    }


    public final RealVector pointLeft;
    public final RealVector pointRight;

    public WallLinear(RealVector pointLeft, RealVector pointRight) {
        this.pointLeft = pointLeft;
        this.pointRight = pointRight;
    }

    public boolean inBoundaries(RealVector point) {
        return super.inBoundaries(this.pointLeft, point, this.pointRight);
    }


    public static class Builder {
        private RealVector pointLeft;
        private RealVector pointRight;

        Builder() {
            this.pointLeft = new ArrayRealVector(2);
            this.pointRight = new ArrayRealVector(2);
        }

        public Builder setPointLeft(double x, double y) {
            this.pointLeft.setEntry(0, x);
            this.pointLeft.setEntry(1, y);
            return this;
        }

        public Builder setPointLeft(RealVector pointLeft) {
            this.pointLeft = pointLeft;
            return this;
        }

        public Builder setPointRight(double x, double y) {
            this.pointRight.setEntry(0, x);
            this.pointRight.setEntry(1, y);
            return this;
        }

        public Builder setPointRight(RealVector pointRight) {
            this.pointRight = pointRight;
            return this;
        }

        public WallLinear build() {
            return new WallLinear(this.pointLeft, this.pointRight);
        }
    }
}