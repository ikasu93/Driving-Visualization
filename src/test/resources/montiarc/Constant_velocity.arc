package montiarc;

import montiarc.atomic.*;

<<Type = "SubSystem">> component Constant_velocity {
	port
		in Double velocity,
		in Double time,
		out Double acceleration,
		out Double steering;


	component Constant<Double>(1) constant;

	component Constant<Double>(0) constant1;

    component Constant<Double>(2) constant2;

    component Constant<Double>(0) constant3;

	component Less<Double> relationalOperator;

	component SwitchB<Double> switchBlock;

	connect constant3.out1 -> steering;
	connect switchBlock.out1 -> acceleration;
	connect relationalOperator.out1 -> switchBlock.cond;
	connect constant2.out1 -> switchBlock.in1;
	connect constant1.out1 -> switchBlock.in3;
	connect time -> relationalOperator.in1;
	connect constant.out1 -> relationalOperator.in2;
}
