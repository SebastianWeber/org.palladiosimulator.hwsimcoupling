package org.palladiosimulator.hwsimcoupling.util.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.palladiosimulator.hwsimcoupling.commands.ExtractionCommand;
import org.palladiosimulator.hwsimcoupling.commands.SimulationCommand;
import org.palladiosimulator.hwsimcoupling.configuration.HWsimCouplingManager;
import org.palladiosimulator.hwsimcoupling.consumers.ErrorConsumer;
import org.palladiosimulator.hwsimcoupling.consumers.OutputConsumer;
import org.palladiosimulator.hwsimcoupling.consumers.VoidConsumer;
import org.palladiosimulator.hwsimcoupling.exceptions.DemandCalculationFailureException;
import org.palladiosimulator.hwsimcoupling.util.CommandHandler;
import org.palladiosimulator.hwsimcoupling.util.DemandCache;
import org.palladiosimulator.hwsimcoupling.util.FileManager;
import org.palladiosimulator.hwsimcoupling.util.MapHelper;

public class DemandCacheImpl implements DemandCache{
	
	public DemandCacheImpl() {
		this.demands = new HashMap<String, String>();
		this.fileManager = new FileManagerImpl();
	}
	
	private HashMap<String, String> demands;
	private FileManager fileManager;
	
	public double get(Map<String, Serializable> parameterMap, RESOURCE resource, CommandHandler commandHandler) {
		parameterMap = HWsimCouplingManager.mergeParameterMapWithProfile(parameterMap, MapHelper.get_required_value_from_map(parameterMap, "hwsim"));
		String key = MapHelper.get_map_as_one_string(parameterMap);
		long processingrate = Long.parseLong(MapHelper.get_required_value_from_map(parameterMap, "processingrate"));
		
		if(!demands.containsKey(key)) {
			try {
				System.out.println("Evaluating demand for key: " + key);
				parameterMap = fileManager.copy_files(parameterMap, commandHandler);
				String demand = simulate(parameterMap, commandHandler);
				if (demand != null) {
					demands.put(key, demand);
					System.out.println("Evaluated demand: " + demand + " for key: " + key);
					return get(demand, resource)/processingrate;
				} else {
					throw new DemandCalculationFailureException("Failed to evaluate demand. Please check the executed commands for errors.");
				}
				
			} catch (IOException | InterruptedException e) {
				throw new DemandCalculationFailureException("Failed to evaluate demand: " + e.getMessage());
			}
		} else {
			return get(demands.get(key), resource)/processingrate;
		}
	}
	
	private double get(String demands, RESOURCE resource) {
		for (String demand : demands.split(";")) {
			if (demand.strip().startsWith(resource.toString())) {
				return Double.parseDouble(demand.strip().replace(resource.toString(), "").strip());
			}
		}
		throw new DemandCalculationFailureException("Failed to evaluate demand.");
	}
	
	
	private String simulate(Map<String, Serializable> parameterMap, CommandHandler commandHandler) throws IOException, InterruptedException {
		
		OutputConsumer demandExtractor = commandHandler.getOutputConsumer();
		ErrorConsumer errorDetector = commandHandler.getErrorConsumer();
		VoidConsumer voidConsumer = new VoidConsumer();
		
		SimulationCommand simulationCommand = commandHandler.getSimulationCommand(parameterMap);
		ExtractionCommand extractionCommand = commandHandler.getExtractionCommand(parameterMap);
		
		CommandExecutor.execute_command(simulationCommand, voidConsumer, errorDetector);
		CommandExecutor.execute_command(extractionCommand, demandExtractor, errorDetector);
		
		return demandExtractor.get_demand();
	}

	
}
