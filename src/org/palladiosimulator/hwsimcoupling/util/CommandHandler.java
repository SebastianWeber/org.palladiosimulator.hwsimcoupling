package org.palladiosimulator.hwsimcoupling.util;

import java.io.Serializable;
import java.util.Map;

import org.palladiosimulator.hwsimcoupling.commands.CopyCommand;
import org.palladiosimulator.hwsimcoupling.commands.ExtractionCommand;
import org.palladiosimulator.hwsimcoupling.commands.SimulationCommand;
import org.palladiosimulator.hwsimcoupling.consumers.ErrorConsumer;
import org.palladiosimulator.hwsimcoupling.consumers.OutputConsumer;

/**
 * @author Sebastian
 * Encapsulates the commands from one coupling
 */
public abstract class CommandHandler {
	
	protected String[] parameters;

	/**
	 * @param parameterMap
	 * @return {@link org.palladiosimulator.hwsimcoupling.commands.CopyCommand} 
	 */
	public abstract CopyCommand getCopyCommand(Map<String, Serializable> parameterMap, String source_path);
	
	/**
	 * @param parameterMap
	 * @return {@link org.palladiosimulator.hwsimcoupling.commands.ExtractionCommand} 
	 */
	public abstract ExtractionCommand getExtractionCommand(Map<String, Serializable> parameterMap);
	
	/**
	 * @param parameterMap
	 * @return {@link org.palladiosimulator.hwsimcoupling.commands.SimulationCommand} 
	 */
	public abstract SimulationCommand getSimulationCommand(Map<String, Serializable> parameterMap);
	
	/**
	 * @return {@link org.palladiosimulator.hwsimcoupling.consumers.OutputConsumer}
	 */
	public abstract OutputConsumer getOutputConsumer();
	
	/**
	 * @return {@link org.palladiosimulator.hwsimcoupling.consumers.ErrorConsumer}
	 */
	public abstract ErrorConsumer getErrorConsumer();
	
	public String[] getParameters() {
		return parameters;
	}
	
	public String getParametersAsString() {
		String parametersAsString = "";
		for (String parameter : parameters) {
			parametersAsString += parameter + ", ";
		}
		return parametersAsString.substring(0, parametersAsString.length() - 2);
	}
	
	
	
}
