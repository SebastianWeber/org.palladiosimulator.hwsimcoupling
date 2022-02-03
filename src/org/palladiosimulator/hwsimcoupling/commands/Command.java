package org.palladiosimulator.hwsimcoupling.commands;

import java.util.List;

public interface Command {

	/**
	 * @return a list of strings that can be used as a parameter for {@link org.palladiosimulator.hwsimcoupling.util.impl.CommandExecutor#execute_command(List, java.util.function.Consumer, java.util.function.Consumer)}
	 */
	public abstract List<String> get_command();
	
}
