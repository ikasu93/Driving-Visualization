/* generated by template generator.InnerComponentWithConstant*/


package visualization.main;

import java.util.LinkedHashMap;
import java.util.Map;
import java.io.IOException;
import de.ma2cfg.simulator.atomic_blocks.Constant;
import de.ma2cfg.simulator.atomic_blocks.Equals;

import de.se_rwth.commons.logging.Log;

public class DoorStatus {
  //inputs
  private Double velocity;
  
  // outputs
  private Boolean status;
	
  // internal variables

  // atomic blocks
  private Constant<Double> idle;
  private Equals<Double> equals;

  // non-atomic blocks

  //connectors
  private Map<String, String> connectors;
  private Map<String, Object> portValues;

  public DoorStatus() throws NumberFormatException, IOException {
    this.idle = new Constant<Double>(0.0);
    this.equals = new Equals<Double>();

    this.portValues = new LinkedHashMap<String, Object>();

    // connectors map is opposite direction
    // than connectors, b/c reading decadency
    // is opposite than writing dependency
    this.connectors = new LinkedHashMap<String, String>(3);
    connectors.put("status", "equals.out1");
    connectors.put("equals.in1", "velocity");
    connectors.put("equals.in2", "idle.out1");
  }

  public void setInputs(Map<String, Object> inputs) {
    if(!(inputs.get("velocity") == null)) {
      velocity = (Double)Log.errorIfNull(inputs.get("velocity"));
      portValues.put("velocity", velocity);
    }
  }

   public Map<String, Object> getOutputs() {
     Map<String, Object> outputs = new LinkedHashMap<String, Object>();
     outputs.put("status", status);
     return outputs;
  }

  public void execute(String block) {
    switch(block.substring(0, block.contains(".")?block.indexOf("."):block.length())) {
      // execute idle block
      case "idle":
        Map<String, Object> idleInputs = new LinkedHashMap<String, Object>();
        for(String inport : idle.getInportNames()) {
          idleInputs.put(inport, portValues.get(connectors.get("idle." + inport)));
        }
        idle.setInputs(idleInputs);

        idle.execute();

        Map<String, Object> idleOutputs = idle.getOutputs();
        for(String key : idleOutputs.keySet()) {
          portValues.put("idle."+ key, idleOutputs.get(key));
        }
        break;
      // execute equals block
      case "equals":
        Map<String, Object> equalsInputs = new LinkedHashMap<String, Object>();
        for(String inport : equals.getInportNames()) {
          equalsInputs.put(inport, portValues.get(connectors.get("equals." + inport)));
        }
        equals.setInputs(equalsInputs);

        equals.execute();

        Map<String, Object> equalsOutputs = equals.getOutputs();
        for(String key : equalsOutputs.keySet()) {
          portValues.put("equals."+ key, equalsOutputs.get(key));
        }
        break;
      default:
        Log.error("block: " + block + ", is not available in: DoorStatus");
        break;
    }
    
    // 2: save output values
    status = (Boolean)portValues.get(connectors.get("status"));
  }
  
  public static String[] getInportNames() {
  	return new String[] {
      "velocity"
    };
  }
}  