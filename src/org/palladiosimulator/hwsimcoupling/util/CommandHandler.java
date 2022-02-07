package org.palladiosimulator.hwsimcoupling.util;

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
	 * @param source_path
	 * @return {@link org.palladiosimulator.hwsimcoupling.commands.CopyCommand} 
	 */
	public CopyCommand getCopyCommand(String source_path);
	
	/**
	 * @param methodname
	 * @return {@link org.palladiosimulator.hwsimcoupling.commands.ExtractionCommand} 
	 */
	public ExtractionCommand getExtractionCommand(String methodname);
	
	/**
	 * @param system 
	 * @param executable
	 * @param methodname
	 * @param parameters
	 * @return {@link org.palladiosimulator.hwsimcoupling.commands.SimulationCommand} 
	 */
	public SimulationCommand getSimulationCommand(String system, String executable, String methodname, String parameters);
	
	/**
	 * @return {@link org.palladiosimulator.hwsimcoupling.consumers.OutputConsumer}
	 */
	public OutputConsumer getOutputConsumer();
	
	/**
	 * @return {@link org.palladiosimulator.hwsimcoupling.consumers.ErrorConsumer}
	 */
	public ErrorConsumer getErrorConsumer();
	
}
