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
public interface CommandHandler {

	/**
	 * @param parameterMap
	 * @return {@link org.palladiosimulator.hwsimcoupling.commands.CopyCommand} 
	 */
	public CopyCommand getCopyCommand(Map<String, Serializable> parameterMap, String source_path);
	
	/**
	 * @param parameterMap
	 * @return {@link org.palladiosimulator.hwsimcoupling.commands.ExtractionCommand} 
	 */
	public ExtractionCommand getExtractionCommand(Map<String, Serializable> parameterMap);
	
	/**
	 * @param parameterMap
	 * @return {@link org.palladiosimulator.hwsimcoupling.commands.SimulationCommand} 
	 */
	public SimulationCommand getSimulationCommand(Map<String, Serializable> parameterMap);
	
	/**
	 * @return {@link org.palladiosimulator.hwsimcoupling.consumers.OutputConsumer}
	 */
	public OutputConsumer getOutputConsumer();
	
	/**
	 * @return {@link org.palladiosimulator.hwsimcoupling.consumers.ErrorConsumer}
	 */
	public ErrorConsumer getErrorConsumer();
	
}
