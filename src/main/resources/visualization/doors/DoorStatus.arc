package visualization.doors;

import visualization.basics.Constant;
import visualization.basics.Equals;

component DoorStatus {
    port
        in Double velocity,
        out Boolean status;

    component Constant<Double>(0.0) idle;
    component Equals<Double> equals;

    connect velocity -> equals.in1;
    connect idle.out1 -> equals.in2;
    connect equals.out1 -> status;
}