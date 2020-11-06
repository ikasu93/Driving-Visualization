package de;

import com.google.common.collect.Lists;
import com.sun.org.apache.regexp.internal.RE;
import de.ma2cfg.simulator.BasicSimulator;
import de.monticore.lang.montiarc.montiarc._symboltable.ComponentSymbol;
import de.monticore.lang.montiarc.montiarc._symboltable.ExpandedComponentInstanceSymbol;
import de.monticore.lang.montiarc.montiarc._symboltable.PortSymbol;
import de.monticore.lang.montiarc.stream._symboltable.NamedStreamSymbol;
import de.rwth.simulink2montiarc.montiarcadapter.Resolver;
import de.rwth.visualization.car.Car;
import de.rwth.visualization.coord.Orientation;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.jscience.physics.amount.Amount;
import org.json.JSONObject;

import javax.measure.quantity.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static javax.measure.unit.NonSI.DEGREE_ANGLE;
import static javax.measure.unit.SI.*;

/**
 * Created by Dinh-An on 21.02.2017.
 */
@WebSocket
public class Simulator {
    private MontiarcToJavaGenerator generator;

    protected Optional<Session> session;

    /**
     * FPS = 1/s
     */
    public final Amount<javax.measure.quantity.Duration> fpsTime =
            Amount.valueOf(1.0, SECOND);

    public SOutput output;

    private Amount<Velocity> v;
    private Amount<javax.measure.quantity.Duration> t;

    public Simulator() {
        /**
         * Initial velocity is 0 m/s
         * Initial time is 0 s
         */
        v = Amount.valueOf(0, METERS_PER_SECOND);
        t = Amount.valueOf(0, SECOND);
    }

    private static final Resolver resolver;

     static {
        Path rPath = null;
        URL srcLocation = Simulator.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            rPath = Paths.get(srcLocation.toURI());
        } catch (URISyntaxException e) {
            Log.error("Could not initialize Trigger resolver at location " + srcLocation);
        }
        resolver = Resolver.get(rPath);
    }

    public Simulator(Path baseDirectory, Path generationDirectory, Path compileDirectory, ExpandedComponentInstanceSymbol inst) {
        this();
         this.generator = new MontiarcToJavaGenerator(baseDirectory, generationDirectory, compileDirectory, inst);
    }

    /**
     * update the new positions after accelerate with a and direction with steering s
     */
    public void update(SInput input){
        calculate(input);
        transmit();
    }

    private double lastSteering = 0.0;

    private void calculate(SInput input) {
        /**
         * Update the time:
         * time = t+(1/20)s, for t=0s
         */
        Amount<Duration> time = t.plus(fpsTime);
        t = time; //update t

        /**
         * Update the velocity:
         * velocity = v+(input)acceleration*(1/20)s, for v=0 m/ss
         */
        Amount<Velocity> velocity = v.plus(input.acceleration.times(fpsTime));
        v = velocity; // update v-> v1
        /**
         * degree = (double)steering*(double)v
         */

        /*
        double degree = Car.getDegree() +
                (-input.steering.doubleValue(DEGREE_ANGLE)) * (v.doubleValue(METERS_PER_SECOND));
        */

        double degree = Car.getDegree() +
                (-input.steering.doubleValue(DEGREE_ANGLE) /*+ lastSteering*/) * (v.doubleValue(METERS_PER_SECOND));
        //lastSteering += -input.steering.doubleValue(DEGREE_ANGLE);

        Amount<Angle> degree1 = Amount.valueOf(degree, DEGREE_ANGLE);
        /**
         * Update the positions x and y:
         * x=(input)x+v*cos((rad)degree)
         * y=(input)y+v*sin((rad)degree)
         */
        Amount<Length> x = input.x0.plus(v.times(fpsTime).times(Math.cos(Math.toRadians(degree))));
        Amount<Length> y = input.y0.plus(v.times(fpsTime).times(Math.sin(Math.toRadians(degree))));


        boolean doorStatus = input.doorStatus;
        boolean indicatorStatus = input.indicatorStatus;
        boolean lightTimerStatus = input.lightTimerStatus;
        boolean triggerStatus = input.triggerStatus;

        output = new SOutput(v, x, y, t, degree1, doorStatus, indicatorStatus, lightTimerStatus, triggerStatus);
        System.out.println("Output: v: "+v+", x: "+x+" ,y: "+y+" ,t: "+t+", degree: "+degree1);
    }

    // send the updated position and the degree to the visualization
    private void transmit() {
        try {
            if(session.isPresent()) {
                JSONObject data = createJSON(output);
                String dataString = data.toString();
                session.get().getRemote().sendString(dataString);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private JSONObject createJSON(SOutput output) {
        JSONObject result = new JSONObject();

        /**
         * Send the client "x=10" instead of "x=10 m"
         * And "angle=45" instead of "angle=45°"
         */
        //
        double xi = output.xi.doubleValue(METER);
        double yi = -output.yi.doubleValue(METER);
        double deg = output.degree.doubleValue(DEGREE_ANGLE);

        result.put("x", xi);
        result.put("y", yi);
        result.put("angle", deg);

        result.put("doorStatus", output.doorStatus);
        result.put("indicatorStatus", output.indicatorStatus);
        result.put("lightTimerStatus", output.lightTimerStatus);
        result.put("triggerStatus", output.triggerStatus);


        return result;
    }

    public Amount<Velocity> getVelocity() {
        return v;
    }

    public Amount<javax.measure.quantity.Duration> getTime(){
        return t;
    }

    //private static int STEP = 1;

    @OnWebSocketConnect
    public void connected(Session session) {

        Simulator simulator = new Simulator();
        simulator.session = Optional.ofNullable(session);

        String root = this.getClass().getResource("").getPath().replaceFirst("/", "").replace("/de", "");

        Path basePath = Paths.get(root);
        Path genPath = Paths.get(root,"gen");
        Path compilePath = Paths.get(root,"gen");

        System.out.println(basePath);
        System.out.println(genPath);
        System.out.println(compilePath);

        //Resolver resolver = Resolver.get(basePath);

        ComponentSymbol cmp =
                resolver.getComponentSymbol("visualization.main.SDCS").orElse(null);
        PortSymbol time = cmp.getIncomingPort("time").orElse(null);
        PortSymbol fl = cmp.getIncomingPort("fl").orElse(null);
        PortSymbol fr = cmp.getIncomingPort("fr").orElse(null);
        PortSymbol slf = cmp.getIncomingPort("slf").orElse(null);
        PortSymbol slb = cmp.getIncomingPort("slb").orElse(null);
        PortSymbol srf = cmp.getIncomingPort("srf").orElse(null);
        PortSymbol srb = cmp.getIncomingPort("srb").orElse(null);
        PortSymbol x_i = cmp.getIncomingPort("x").orElse(null);
        PortSymbol y_i = cmp.getIncomingPort("y").orElse(null);
        PortSymbol velocity = cmp.getIncomingPort("velocity").orElse(null);

        double flDistance =
                Car.getSensor(Orientation.FRONT_LEFT).getMinDistance();
        double frDistance =
                Car.getSensor(Orientation.FRONT_RIGHT).getMinDistance();
        double slfDistance =
                Car.getSensor(Orientation.FRONT_LEFT_SIDE).getMinDistance();
        double slbDistance =
                Car.getSensor(Orientation.BACK_LEFT_SIDE).getMinDistance();
        double srfDistance =
                Car.getSensor(Orientation.FRONT_RIGHT_SIDE).getMinDistance();
        double srbDistance =
                Car.getSensor(Orientation.BACK_RIGHT_SIDE).getMinDistance();

        NamedStreamSymbol timeSymbol =
                time.addStream(0, true, Lists.newArrayList(0.0));
        NamedStreamSymbol flSymbol =
                fl.addStream(0, true, Lists.newArrayList(flDistance));
        NamedStreamSymbol frSymbol =
                fr.addStream(0, true, Lists.newArrayList(frDistance));
        NamedStreamSymbol slfSymbol =
                slf.addStream(0, true, Lists.newArrayList(slfDistance));
        NamedStreamSymbol slbSymbol =
                slb.addStream(0, true, Lists.newArrayList(slbDistance));
        NamedStreamSymbol srfSymbol =
                srf.addStream(0, true, Lists.newArrayList(srfDistance));
        NamedStreamSymbol srbSymbol =
                srb.addStream(0, true, Lists.newArrayList(srbDistance));
        NamedStreamSymbol velocitySymbol =
                velocity.addStream(0, true, Lists.newArrayList(0.0));
        NamedStreamSymbol xSymbol =
                x_i.addStream(0, true, Lists.newArrayList(0.0));
        NamedStreamSymbol ySymbol =
                y_i.addStream(0, true, Lists.newArrayList(0.0));

        ExpandedComponentInstanceSymbol inst =
                resolver.getExpandedComponentInstanceSymbol("visualization.main.sDCS").orElse(null);
        System.out.println(inst.getFullName());
        BasicSimulator sim = new BasicSimulator(basePath, genPath, compilePath, inst);

        Map<String, Object[]> outputs = new HashMap<>();

        try {
            outputs = sim.execute();
        } catch(Exception e) {
            e.printStackTrace();
        }

        Double steering = (Double)outputs.get("steering")[0];
        Double acceleration = (Double)outputs.get("acceleration")[0];
        Boolean lightStatus = (Boolean)outputs.get("lightStatus")[0];
        Boolean indicatorStatus = (Boolean)outputs.get("indicatorStatus")[0];
        Boolean doorStatus = (Boolean)outputs.get("doorStatus")[0];
        Boolean triggerStatus = (Boolean)outputs.get("triggerStatus")[0];

        Amount<Acceleration> a = Amount.valueOf(0.0, METERS_PER_SQUARE_SECOND);
        Amount<Angle> s = Amount.valueOf(0.0, DEGREE_ANGLE);
        Amount<Length> x = Amount.valueOf(0, METER);
        Amount<Length> y = Amount.valueOf(0, METER);
        Amount<Duration> t = Amount.valueOf(0, SECOND);

        //boolean triggerStatus = false;

        // (a,s)->SInput->SOutput
        SInput input = new SInput(a, s, x, y, t, doorStatus, indicatorStatus,
                lightStatus, triggerStatus); //a,s will be updated
        simulator.update(input);
        SOutput output = simulator.output; //SOutput will be filled with updated values

        // Give the updated t and v to the Generator/BasicSimulator and next loop
        //Amount<Length> di = Amount.valueOf(0, METER); // tmp di


        while(true) {
            time.removeStream(timeSymbol);
            fl.removeStream(flSymbol);
            fr.removeStream(frSymbol);
            slf.removeStream(slfSymbol);
            slb.removeStream(slbSymbol);
            srf.removeStream(srfSymbol);
            srb.removeStream(srbSymbol);
            x_i.removeStream(xSymbol);
            y_i.removeStream(ySymbol);

            double v = output.velocity.doubleValue(METERS_PER_SECOND);
            double ti = output.ti.doubleValue(SECOND);
            double xi = output.xi.doubleValue(METER);
            double yi = output.yi.doubleValue(METER);
            double degree = output.degree.doubleValue(DEGREE_ANGLE);
            boolean ds = output.doorStatus;
            boolean is = output.indicatorStatus;
            boolean ls = output.lightTimerStatus;
            boolean ts = output.triggerStatus;

            Car.setPosition(xi, yi);
            Car.setDegree(degree);

            flDistance =
                    Car.getSensor(Orientation.FRONT_LEFT).getMinDistance();
            frDistance =
                    Car.getSensor(Orientation.FRONT_RIGHT).getMinDistance();
            slfDistance =
                    Car.getSensor(Orientation.FRONT_LEFT_SIDE).getMinDistance();
            slbDistance =
                    Car.getSensor(Orientation.BACK_LEFT_SIDE).getMinDistance();
            double blDistance =
                    Car.getSensor(Orientation.BACK_LEFT).getMinDistance();
            double brDistance =
                    Car.getSensor(Orientation.BACK_RIGHT).getMinDistance();
            srfDistance =
                    Car.getSensor(Orientation.FRONT_RIGHT_SIDE).getMinDistance();
            srbDistance =
                    Car.getSensor(Orientation.BACK_RIGHT_SIDE).getMinDistance();

            System.out.println("fl: "+flDistance);
            System.out.println("fr: "+frDistance);
            System.out.println("slf: "+slfDistance);
            System.out.println("slb: "+slbDistance);
            System.out.println("bl: "+blDistance);
            System.out.println("br: "+brDistance);
            System.out.println("srf: "+srfDistance);
            System.out.println("srb: "+srbDistance);

            timeSymbol =
                    time.addStream(0, true, Lists.newArrayList(ti));
            flSymbol =
                    fl.addStream(0, true, Lists.newArrayList(flDistance));
            frSymbol =
                    fr.addStream(0, true, Lists.newArrayList(frDistance));
            slfSymbol =
                    slf.addStream(0, true, Lists.newArrayList(slfDistance));
            slbSymbol =
                    slb.addStream(0, true, Lists.newArrayList(slbDistance));
            srfSymbol =
                    srf.addStream(0, true, Lists.newArrayList(srfDistance));
            srbSymbol =
                    srb.addStream(0, true, Lists.newArrayList(srbDistance));
            velocitySymbol =
                    velocity.addStream(0, true, Lists.newArrayList(v));
            xSymbol =
                    x_i.addStream(0, true, Lists.newArrayList(xi));
            ySymbol =
                    y_i.addStream(0, true, Lists.newArrayList(yi));

            outputs = new HashMap<>();

            try {
                outputs = sim.execute();
            } catch(Exception e) {
                e.printStackTrace();
            }

            steering = (Double)outputs.get("steering")[0];
            acceleration = (Double)outputs.get("acceleration")[0];
            lightStatus = (Boolean)outputs.get("lightStatus")[0];
            indicatorStatus = (Boolean)outputs.get("indicatorStatus")[0];
            doorStatus = (Boolean)outputs.get("doorStatus")[0];
            triggerStatus = (Boolean)outputs.get("triggerStatus")[0];

            System.out.println("Steering=" + steering);

            a = Amount.valueOf(acceleration, METERS_PER_SQUARE_SECOND);
            s = Amount.valueOf(steering, DEGREE_ANGLE);
            x = Amount.valueOf(xi, METER);
            y = Amount.valueOf(yi, METER);
            t = Amount.valueOf(ti, SECOND);

            // (a,s)->SInput->SOutput
            input = new SInput(a, s, output.xi, output.yi, output.ti, doorStatus,
                    indicatorStatus, lightStatus, triggerStatus); //a,s will be updated
            simulator.update(input);
            output = simulator.output; //SOutput will be filled with updated values

            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @OnWebSocketClose
    public void closed(Session   session, int statusCode, String reason) {
        this.session = Optional.empty();
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
    }

    public void generate() {
        try {
            generator.generate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getOutputs(Amount<Velocity> v, Amount<javax.measure.quantity.Duration> t) throws Exception {
        double velocity = v.doubleValue(METERS_PER_SECOND);
        double time = t.doubleValue(SECOND);

        Map<String, Object> input = new HashMap<>();
        input.put("velocity", velocity);
        input.put("time", time);

        Map<String, Object> output = generator.calculateOutput(input);

        Double acceleration = (Double) output.get("acceleration");
        Double steering = (Double) output.get("steering");
        System.out.println("a: "+acceleration+", s: "+steering);
    }

    public void getOutputs(Amount<Angle> angle, Amount<Length> x, Amount<Length> y,
                           Amount<Length> d1, Amount<Length> d2, Amount<Length> d3, Amount<Length> d4,
                           Amount<Length> d5, Amount<Length> d6, Amount<Length> d7, Amount<Length> d8)
            throws Exception {
        double angleDouble = angle.doubleValue(DEGREE_ANGLE);
        double xDouble = x.doubleValue(METER);
        double yDouble = y.doubleValue(METER);

        double d1Double = d1.doubleValue(METER);
        double d2Double = d2.doubleValue(METER);
        double d3Double = d3.doubleValue(METER);
        double d4Double = d4.doubleValue(METER);
        double d5Double = d5.doubleValue(METER);
        double d6Double = d6.doubleValue(METER);
        double d7Double = d7.doubleValue(METER);
        double d8Double = d8.doubleValue(METER);

        Map<String, Object> input = new HashMap<>();
        input.put("", angleDouble);
        input.put("", xDouble);
        input.put("", yDouble);
        input.put("", d1Double);
        input.put("", d2Double);
        input.put("", d3Double);
        input.put("", d4Double);
        input.put("", d5Double);
        input.put("", d6Double);
        input.put("", d7Double);
        input.put("", d8Double);

        Map<String, Object> output = generator.calculateOutput(input);
        Double acceleration = (Double) output.get("acceleration");
        Double steering = (Double) output.get("steering");

        System.out.println("a: "+acceleration+", s: "+steering);
    }
}
