package org.palladiosimulator.hwsimcoupling.util.impl;

import java.io.IOException;
import java.util.function.Consumer;

import org.palladiosimulator.hwsimcoupling.commands.CopyCommand;
import org.palladiosimulator.hwsimcoupling.exceptions.DemandCalculationFailureException;
import org.palladiosimulator.hwsimcoupling.util.CommandHandler;
import org.palladiosimulator.hwsimcoupling.util.FileManager;

public class FileManagerImpl implements FileManager {
	
	private CommandHandler commandHandler;
	
	public FileManagerImpl(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}
	
	public String sync_file(String path) {
		if (path.startsWith(LOCATIONS.LOCAL.toString())) {
			return strip_path(path);
		} else if (path.startsWith(LOCATIONS.COPY.toString())) {
			try {
				CopyCommand copyCommand = commandHandler.getCopyCommand(strip_path(path));
				CommandExecutor.execute_command(copyCommand.get_command(), commandHandler.getOutputConsumer(), commandHandler.getErrorConsumer());
				return copyCommand.get_destination();
			} catch (IOException | InterruptedException e) {
				throw new DemandCalculationFailureException("Failed to copy file " + path + ": " + e.getMessage());
			}
		} else {
			throw new DemandCalculationFailureException("File location of " + path + " is unknown. Please use " + LOCATIONS.get_locations_string() + ".");
		}
	}
	
	public String[] sync_files(String[] paths) {
		String[] paths_return = new String[paths.length];
		for (int i = 0; i < paths.length; i++) {
			paths_return[i] = sync_file(paths[i]);
		}
		return paths_return;
	}
	
	public String strip_path(String path) {
		if (path.startsWith(LOCATIONS.LOCAL.toString())) {
			return path.replaceFirst(LOCATIONS.LOCAL.toString(), "");
		} else if (path.startsWith(LOCATIONS.COPY.toString())) {
			return path.replaceFirst(LOCATIONS.COPY.toString(), "");
		} else {
			return path;
		}
	}
	
	private enum LOCATIONS {
		
		LOCAL("local:"), COPY("copy:");
		
		private final String keyword;
		
		private LOCATIONS(String keyword) {
			this.keyword = keyword;
		}
		
		public String toString() {
			return keyword;
		}
		
		public static String get_locations_string() {
			return "\"" + LOCATIONS.LOCAL.toString() + "\" or \"" + LOCATIONS.COPY.toString() + "\"";
		}
		
	}

}
