package org.palladiosimulator.hwsimcoupling.commands;

public interface CopyCommand extends Command{
	
	/**
	 * @return the absolute path where the file has been copied to
	 */
	public String get_destination();
	
}
