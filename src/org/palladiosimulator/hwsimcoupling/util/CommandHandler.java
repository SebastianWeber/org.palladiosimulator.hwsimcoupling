package org.palladiosimulator.hwsimcoupling.util;

import org.palladiosimulator.hwsimcoupling.commands.CopyCommand;
import org.palladiosimulator.hwsimcoupling.commands.ExtractionCommand;
import org.palladiosimulator.hwsimcoupling.commands.SimulationCommand;
import org.palladiosimulator.hwsimcoupling.consumers.ErrorConsumer;
import org.palladiosimulator.hwsimcoupling.consumers.OutputConsumer;

public interface CommandHandler {

	public CopyCommand getCopyCommand(String source_path);
	public ExtractionCommand getExtractionCommand(String methodname);
	public SimulationCommand getSimulationCommand(String system, String executable, String methodname, String parameters);
	public OutputConsumer getOutputConsumer();
	public ErrorConsumer getErrorConsumer();
	
}
