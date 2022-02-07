package org.palladiosimulator.hwsimcoupling.util.impl;

import java.io.IOException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.palladiosimulator.hwsimcoupling.commands.CopyCommand;
import org.palladiosimulator.hwsimcoupling.exceptions.DemandCalculationFailureException;
import org.palladiosimulator.hwsimcoupling.util.CommandHandler;
import org.palladiosimulator.hwsimcoupling.util.FileManager;

public class FileManagerImpl implements FileManager {

	private CommandHandler commandHandler;
	
	public FileManagerImpl(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}
	
	public String copy_file(String path) {
		if (path.startsWith(LOCATIONS.MANUAL.toString())) {
			return strip_path(path);
		} else if (path.startsWith(LOCATIONS.ABSOLUTE.toString())) {
			try {
				CopyCommand copyCommand = commandHandler.getCopyCommand(strip_path(path));
				CommandExecutor.execute_command(copyCommand, commandHandler.getOutputConsumer(), commandHandler.getErrorConsumer());
				return copyCommand.get_destination();
			} catch (IOException | InterruptedException e) {
				throw new DemandCalculationFailureException("Failed to copy file " + path + ": " + e.getMessage());
			}
		} else if (path.startsWith(LOCATIONS.LOCAL.toString())) {
			try {
				String stripped_path = strip_path(path);
				String project_name = stripped_path.split("/")[0];
				String project_path = ResourcesPlugin.getWorkspace().getRoot().getProject(project_name).getLocation().toString();
				String source_path = stripped_path.replace(project_name, project_path);
				CopyCommand copyCommand = commandHandler.getCopyCommand(source_path);
				CommandExecutor.execute_command(copyCommand, commandHandler.getOutputConsumer(), commandHandler.getErrorConsumer());
				return copyCommand.get_destination();
			} catch (IOException | InterruptedException e) {
				throw new DemandCalculationFailureException("Failed to copy file " + path + ": " + e.getMessage());
			}
		} else {
			throw new DemandCalculationFailureException("File location of " + path + " is unknown. Please use " + LOCATIONS.get_locations_string() + ".");
		}
	}
	
	public String[] copy_files(String[] paths) {
		String[] paths_return = new String[paths.length];
		for (int i = 0; i < paths.length; i++) {
			paths_return[i] = copy_file(paths[i]);
		}
		return paths_return;
	}
	
	public String strip_path(String path) {
		if (path.startsWith(LOCATIONS.LOCAL.toString())) {
			return path.replaceFirst(LOCATIONS.LOCAL.toString(), "");
		} else if (path.startsWith(LOCATIONS.MANUAL.toString())) {
			return path.replaceFirst(LOCATIONS.MANUAL.toString(), "");
		} else if (path.startsWith(LOCATIONS.ABSOLUTE.toString())) {
			return path.replaceFirst(LOCATIONS.ABSOLUTE.toString(), "");
		} else {
			return path;
		}
	}
	


}
