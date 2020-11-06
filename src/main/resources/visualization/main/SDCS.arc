package visualization.main;

import visualization.basics.*;
import visualization.doors.*;
import visualization.lights.*;
import visualization.movement.*;
import visualization.feature.*;

component SDCS {
    port
        in Double time,
        in Double fl,
        in Double fr,
        in Double slf,
        in Double slb,
        in Double srf,
        in Double srb,
        in Double velocity,
        in Double x,
        in Double y,
        out Double steering,
        out Double acceleration,
        out Boolean lightStatus,
        out Boolean indicatorStatus,
        out Boolean triggerStatus,
        out Boolean brakelightStatus,
        out Boolean doorStatus;


    component SteeringControl steeringControl;
    component ConstantVelocity constantVelocity;
    component IndicatorStatus indStatus;
    component LightTimer lightTimer;
    component DoorStatus ds;
    component GameOverTrigger trigger;
   // component BrakelightControl brakelightControl;

    connect time->constantVelocity.time;
    connect velocity->constantVelocity.velocity;
    connect fl->steeringControl.fl;
    connect fr->steeringControl.fr;
    connect slf->steeringControl.slf;
    connect slb->steeringControl.slb;
    connect srf->steeringControl.srf;
    connect srb->steeringControl.srb;
    connect time->indStatus.time;
    connect velocity->ds.velocity;
    connect time->lightTimer.time;
    connect x->trigger.x;
    connect y->trigger.y;
    //connect time->brakelightControl;

    connect constantVelocity.acceleration->acceleration;
    connect steeringControl.steering->steering;
    connect indStatus.status->indicatorStatus;
    connect ds.status->doorStatus;
    connect lightTimer.status->lightStatus;
    connect trigger.status->triggerStatus;
   // connect brakelightControl.status->brakelightStatus;
}