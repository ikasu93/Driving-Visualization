package visualization.movement;

import visualization.basics.Constant;
import visualization.basics.Less;
import visualization.basics.SwitchB;

<<Type = "SubSystem">> component ConstantVelocity {
	port
		in Double velocity,
		in Double time,
		out Double acceleration;

	component Constant<Double>(1) constant;

	component Constant<Double>(0) constant1;

    component Constant<Double>(2) constant2;

	component Less<Double> relationalOperator;

	component SwitchB<Double> switchBlock;

	connect switchBlock.out1 -> acceleration;
	connect relationalOperator.out1 -> switchBlock.cond;
	connect constant2.out1 -> switchBlock.in1;
	connect constant1.out1 -> switchBlock.in3;
	connect time -> relationalOperator.in1;
	connect constant.out1 -> relationalOperator.in2;
}
