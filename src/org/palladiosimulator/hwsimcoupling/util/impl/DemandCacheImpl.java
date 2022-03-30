package org.palladiosimulator.hwsimcoupling.util.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.palladiosimulator.hwsimcoupling.CacheInitializer;
import org.palladiosimulator.hwsimcoupling.commands.ExtractionCommand;
import org.palladiosimulator.hwsimcoupling.commands.SimulationCommand;
import org.palladiosimulator.hwsimcoupling.configuration.DemandHelper;
import org.palladiosimulator.hwsimcoupling.configuration.Parameter;
import org.palladiosimulator.hwsimcoupling.configuration.PersistenceManager;
import org.palladiosimulator.hwsimcoupling.configuration.ProfileCache;
import org.palladiosimulator.hwsimcoupling.consumers.ErrorConsumer;
import org.palladiosimulator.hwsimcoupling.consumers.OutputConsumer;
import org.palladiosimulator.hwsimcoupling.consumers.VoidConsumer;
import org.palladiosimulator.hwsimcoupling.exceptions.DemandCalculationFailureException;
import org.palladiosimulator.hwsimcoupling.exceptions.MissingParameterException;
import org.palladiosimulator.hwsimcoupling.util.CommandHandler;
import org.palladiosimulator.hwsimcoupling.util.DemandCache;
import org.palladiosimulator.hwsimcoupling.util.MapHelper;

import de.uka.ipd.sdq.simucomframework.variables.StackContext;
import de.uka.ipd.sdq.simucomframework.variables.converter.NumberConverter;

public final class DemandCacheImpl implements DemandCache {

    protected static final Logger LOGGER = org.apache.log4j.Logger.getLogger(DemandCacheImpl.class);
    protected static List<String> loggedWarnings = new ArrayList<String>();

    private Map<String, Map<String, String>> demands;
    private Map<String, String> hashValues;
    private ProfileCache profileCache;

    public DemandCacheImpl(ProfileCache profileCache) {
        this.demands = PersistenceManager.loadDemands();
        this.profileCache = profileCache;
        this.hashValues = new HashMap<String, String>();
    }

    public void saveDemands() {
        LOGGER.warn("Demands saved");
        PersistenceManager.saveDemands(demands);
    }

    public Map<String, Map<String, String>> getDemands() {
        return demands;
    }

    public Map<String, String> getSerializedDemands() {
        Map<String, String> serializedDemands = new HashMap<String, String>();
        for (Entry<String, Map<String, String>> demand : demands.entrySet()) {
            serializedDemands.put(demand.getKey(), DemandHelper.serializeDemand(demand.getValue()));
        }
        return serializedDemands;
    }

    public void addDemand(String key, String value) {
        demands.put(key, DemandHelper.deserializeDemand(value));
        CacheInitializer.getInstance()
            .initSaveDemands();
    }

    public void removeDemand(String key) {
        demands.remove(key);
        saveDemands();
    }

    public void clearCache() {
        this.demands = new HashMap<String, Map<String, String>>();
        saveDemands();
    }

    public double get(Map<String, Serializable> parameterMap, RESOURCE resource, CommandHandler commandHandler) {
        Serializable containerID = parameterMap.get(Parameter.CONTAINERID.getKeyword());
        String profile = parameterMap
            .computeIfAbsent(Parameter.PROFILE.getKeyword(), x -> profileCache.getProfile(containerID.toString()))
            .toString();
        parameterMap = profileCache.mergeParameterMapWithProfile(parameterMap, profile);
        Map<String, String> parameterMapHashValues = FileManager.getHashValuesFromFiles(parameterMap, hashValues);
        hashValues.putAll(parameterMapHashValues);
        parameterMap.putAll(parameterMapHashValues);

        String key = MapHelper.getMapAsOneString(parameterMap);
        long processingrate = Long
            .parseLong(MapHelper.getRequiredValueFromMap(parameterMap, Parameter.PROCESSINGRATE.getKeyword()));

        if (!demands.containsKey(key)) {
            try {
                LOGGER.warn("Evaluating demand for key: " + key);
                parameterMap = FileManager.copyFiles(parameterMap, commandHandler);
                String demand = simulate(parameterMap, commandHandler);
                if (demand != null) {
                    addDemand(key, demand);
                    LOGGER.warn("Evaluated demand: " + demand + " for key: " + key);
                    return getDemand(key, resource, processingrate);
                } else {
                    throw new DemandCalculationFailureException(
                            "Failed to evaluate demand. Please check the executed commands for errors.");
                }

            } catch (IOException | InterruptedException e) {
                throw new DemandCalculationFailureException("Failed to evaluate demand: " + e.getMessage());
            }
        } else {
            return getDemand(key, resource, processingrate);
        }

    }

    private double getDemand(String key, RESOURCE resource, long processingrate) {
        return evaluateDemand(demands.get(key)
            .get(resource.toString())) / processingrate;
    }

    private double evaluateDemand(String demand) {
        return NumberConverter.toDouble(StackContext.evaluateStatic(demand, Double.class));
    }

    private String simulate(Map<String, Serializable> parameterMap, CommandHandler commandHandler)
            throws IOException, InterruptedException {

        OutputConsumer demandExtractor = commandHandler.getOutputConsumer();
        ErrorConsumer errorDetector = commandHandler.getErrorConsumer();
        VoidConsumer voidConsumer = new VoidConsumer();

        SimulationCommand simulationCommand = commandHandler.getSimulationCommand(parameterMap);
        ExtractionCommand extractionCommand = commandHandler.getExtractionCommand(parameterMap);

        try {
            CommandExecutor.executeCommand(simulationCommand, voidConsumer, errorDetector);
            CommandExecutor.executeCommand(extractionCommand, demandExtractor, errorDetector);
        } catch (MissingParameterException | DemandCalculationFailureException e ) {
            LOGGER.error(e.getMessage());
            throw e;
        } 
        return demandExtractor.getDemand();
    }

}
