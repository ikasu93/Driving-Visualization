package visualization.feature;

import visualization.basics.Constant;
import visualization.basics.Greater;
import visualization.basics.Less;
import visualization.basics.Or;

component GameOverTrigger {
    port
        in Double x,
        in Double y,
        out Boolean status;

    component Constant<Double>(-150) leftBoundary;
    component Constant<Double>(150) rightBoundary;
    component Constant<Double>(200) aboveBoundary;
    component Constant<Double>(-200) belowBoundary;

    component Greater<Double> greater1;
    component Greater<Double> greater2;

    component Less<Double> less1;
    component Less<Double> less2;

    component Or<Boolean> or1;
    component Or<Boolean> or2;
    component Or<Boolean> or3;

    connect x -> greater1.in1;
    connect aboveBoundary.out1 -> greater1.in2;
    connect x -> less1.in1;
    connect belowBoundary.out1 -> less1.in2;
    connect greater1.out1 -> or1.in1;
    connect less1.out1 -> or1.in2;

    connect y -> greater2.in1;
    connect rightBoundary.out1 -> greater2.in2;
    connect y -> less2.in1;
    connect leftBoundary.out1 -> less2.in2;
    connect greater2.out1 -> or2.in1;
    connect less2.out1 -> or2.in2;

    connect or1.out1 -> or3.in1;
    connect or2.out1 -> or3.in2;
    connect or3.out1 -> status;
}