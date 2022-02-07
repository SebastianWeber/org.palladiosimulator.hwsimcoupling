package org.palladiosimulator.hwsimcoupling.util.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.palladiosimulator.hwsimcoupling.commands.ExtractionCommand;
import org.palladiosimulator.hwsimcoupling.commands.SimulationCommand;
import org.palladiosimulator.hwsimcoupling.consumers.ErrorConsumer;
import org.palladiosimulator.hwsimcoupling.consumers.OutputConsumer;
import org.palladiosimulator.hwsimcoupling.consumers.VoidConsumer;
import org.palladiosimulator.hwsimcoupling.exceptions.DemandCalculationFailureException;
import org.palladiosimulator.hwsimcoupling.exceptions.MissingParameterException;
import org.palladiosimulator.hwsimcoupling.util.CommandHandler;
import org.palladiosimulator.hwsimcoupling.util.FileManager;

public class DemandCacheImpl {
	private HashMap<String, Double> demands;
	private FileManager fileManager;
	private CommandHandler commandHandler;
	
	public DemandCacheImpl(CommandHandler commandHandler) {
		this.demands = new HashMap<String, Double>();
		this.commandHandler = commandHandler;
		this.fileManager = new FileManagerImpl(commandHandler);
	}
	
	public double get(Map<String, Serializable> parameterMap) {	
		String executable = get_required_value_from_map(parameterMap, "executable");
		String system = get_required_value_from_map(parameterMap, "system");
		String methodname = get_required_value_from_map(parameterMap, "methodname");
		String parameters = get_required_value_from_map(parameterMap, "parameter");
		long processingrate = Long.parseLong(get_required_value_from_map(parameterMap, "processingrate"));
		
		String key = system + " " + executable + " " + methodname + " " + parameters;
		
		if(!demands.containsKey(key)) {
			try {
				System.out.println("Evaluating demand for: " + key + ".");
				String[] paths = fileManager.copy_files(new String[] {system, executable});
				system = paths[0];
				executable = paths[1];
				double demand = simulate(system, executable, methodname, parameters);
				demands.put(key, demand);
				return demand/processingrate;
			} catch (IOException | InterruptedException e) {
				throw new DemandCalculationFailureException("Failed to evaluate demand: " + e.getMessage());
			}
		} else {
			return demands.get(key)/processingrate;
		}
	}
	
	private String get_required_value_from_map(Map<String, Serializable> map, String key) {
		if (map.get(key) != null) {
			return String.valueOf(map.get(key)).replaceAll("\"", "");
		} else {
			throw new MissingParameterException("The required parameter " + key + " is missing.");
		}
		
	}
	
	private double simulate(String system, String executable, String methodname, String parameters) throws IOException, InterruptedException {
		
		OutputConsumer demandExtractor = commandHandler.getOutputConsumer();
		ErrorConsumer errorDetector = commandHandler.getErrorConsumer();
		VoidConsumer voidConsumer = new VoidConsumer();
		
		SimulationCommand simulationCommand = commandHandler.getSimulationCommand(system, executable, methodname, parameters);
		ExtractionCommand extractionCommand = commandHandler.getExtractionCommand(methodname);
		
		CommandExecutor.execute_command(simulationCommand, voidConsumer, errorDetector);
		CommandExecutor.execute_command(extractionCommand, demandExtractor, errorDetector);
		
		return demandExtractor.get_demand();
	}

	
}
