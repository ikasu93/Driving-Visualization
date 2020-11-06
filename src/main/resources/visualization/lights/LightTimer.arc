package visualization.lights;

import visualization.basics.Constant;
import visualization.basics.Greater;

component LightTimer {
    port
        in Double time,
        out Boolean status;

    component Constant<Double>(1.0) timeout;
    component Greater<Double> greater;

    connect time -> greater.in1;
    connect timeout.out1 -> greater.in2;
    connect greater.out1 -> status;
}