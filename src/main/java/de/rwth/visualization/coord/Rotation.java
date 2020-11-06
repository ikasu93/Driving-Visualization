package de.rwth.visualization.coord;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public abstract class Rotation {
    // Rotation matrix
    public static Array2DRowRealMatrix getMatrix(double degree) {
        Array2DRowRealMatrix rotationMatrix = new Array2DRowRealMatrix(2,2);

        double degree1 = Math.toRadians(degree);

        // im Uhrzeigersinn drehen
        rotationMatrix.setEntry(0,0, Math.cos(degree1));
        rotationMatrix.setEntry(0,1, Math.sin(degree1));
        rotationMatrix.setEntry(1,0, -Math.sin(degree1));
        rotationMatrix.setEntry(1,1, Math.cos(degree1));

        return rotationMatrix;
    }
}