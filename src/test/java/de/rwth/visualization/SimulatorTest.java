package de.rwth.visualization;

import com.google.common.collect.Lists;
import de.Simulator;
import de.ma2cfg.simulator.BasicSimulator;
import de.monticore.lang.montiarc.montiarc._symboltable.ComponentSymbol;
import de.monticore.lang.montiarc.montiarc._symboltable.ExpandedComponentInstanceSymbol;
import de.monticore.lang.montiarc.montiarc._symboltable.PortSymbol;
import de.rwth.simulink2montiarc.montiarcadapter.Resolver;
import org.jscience.physics.amount.Amount;
import org.junit.Test;

import javax.measure.quantity.Velocity;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

import static javax.measure.unit.SI.METERS_PER_SECOND;
import static javax.measure.unit.SI.SECOND;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Dinh-An on 11.04.2017.
 */
public class SimulatorTest {
    Path basePath = Paths.get("src/main/resources");
    Path genPath = Paths.get("src/main/resources/gen/");
    Path compilePath = Paths.get("src/main/resources/gen/");

    @Test
    public void testGenerator() throws Exception {
        Resolver resolver = Resolver.get("src/test/resources");
        ComponentSymbol con = resolver.getComponentSymbol("montiarc.atomic.Constant").orElse(null);
        assertNotNull(con);
        ComponentSymbol cmp = resolver.getComponentSymbol("montiarc.Constant_velocity").orElse(null);
        assertNotNull(cmp);

        PortSymbol velocity = cmp.getIncomingPort("velocity").orElse(null);
        PortSymbol time = cmp.getIncomingPort("time").orElse(null);
        assertNotNull(velocity);
        assertNotNull(time);
        //set inport stream
        velocity.addStream(0, true, Lists.newArrayList(0.0));
        time.addStream(0, true, Lists.newArrayList(0.0));

        ExpandedComponentInstanceSymbol inst = resolver.getExpandedComponentInstanceSymbol("montiarc.constant_velocity").orElse(null);
        assertNotNull(inst);

        BasicSimulator sim = new BasicSimulator(basePath, genPath, compilePath, inst);
        Map<String, Object[]> outputs = sim.execute();
        System.out.println(Arrays.toString(outputs.get("acceleration")));
        System.out.println(Arrays.toString(outputs.get("steering")));
        Double acceleration = (Double) outputs.get("acceleration")[0];
        Double steering = (Double) outputs.get("steering")[0];
    }

    @Test
    public void getOutputs() throws Exception {
        Resolver resolver = Resolver.get("src/test/resources");
        ExpandedComponentInstanceSymbol inst = resolver.getExpandedComponentInstanceSymbol("montiarc.constant_velocity").orElse(null);

        Amount<Velocity> v = Amount.valueOf(2, METERS_PER_SECOND);
        Amount<javax.measure.quantity.Duration> t = Amount.valueOf(0, SECOND);

        Simulator simulator = new Simulator(basePath, genPath, compilePath, inst);
        simulator.generate();
        simulator.getOutputs(v, t);
    }
}
