package de;

import de.ma2cfg.helper.Names;
import de.ma2cfg.simulator.BasicSimulator;
import de.monticore.lang.montiarc.montiarc._symboltable.ExpandedComponentInstanceSymbol;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

/**
 * Created by WindowsServer1.1 on 02.05.2017.
 */
public class MontiarcToJavaGenerator extends BasicSimulator {
    // generates the montiarc classes ones and stores the data(methods and classes) in generatedClass
    private GeneratedClass generatedClass;

    // calls the constructor of BasicSimulator
    public MontiarcToJavaGenerator(Path baseDirectory, Path generationDirectory, Path compileDirectory, String componentName, String... modelPath) {
        super(baseDirectory, generationDirectory, compileDirectory, componentName, modelPath);
    }

    public MontiarcToJavaGenerator(Path baseDirectory, Path generationDirectory, Path compileDirectory, ExpandedComponentInstanceSymbol inst) {
        super(baseDirectory, generationDirectory, compileDirectory, inst);
    }

    // generates Java code out of the MontiArc file
    public void generate() throws Exception {
        prepare();
        //generateComponents(getInst());
        compileGeneratedFiles();
        //generatedClass = new GeneratedClass(loadClass(Names.getComponentName(getInst().getFullName())));
    }

    // calculate the output
    public Map<String, Object> calculateOutput(Map<String, Object> input)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Objects.requireNonNull(generatedClass, "Model source has to be generated first");
        generatedClass.setInputs(input);
        generatedClass.execute();
        return generatedClass.getOutput();
    }

    private class GeneratedClass {
        private final Method setInputs;
        private final Method execute;
        private final Method getOutputs;
        private final Class<?> symClass;
        private final Object symClassObj;

        GeneratedClass(Class<?> generatedClass)
                throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            this.symClass = generatedClass;
            Constructor<?> constructor = symClass.getConstructor();
            this.symClassObj = constructor.newInstance();

            this.setInputs = symClass.getMethod("setInputs", Map.class);
            this.execute = symClass.getMethod("execute");
            this.getOutputs = symClass.getMethod("getOutputs");
        }

        void setInputs(Map<String, Object> input) throws InvocationTargetException, IllegalAccessException {
            setInputs.invoke(symClassObj, input);
        }

        void execute() throws InvocationTargetException, IllegalAccessException {
            execute.invoke(symClassObj);
        }

        Map<String, Object> getOutput() throws InvocationTargetException, IllegalAccessException {
            //noinspection unchecked
            return (Map<String, Object>) getOutputs.invoke(symClassObj);
        }
    }
}
