package org.palladiosimulator.hwsimcoupling.util.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.ResourcesPlugin;
import org.palladiosimulator.hwsimcoupling.commands.CopyCommand;
import org.palladiosimulator.hwsimcoupling.exceptions.DemandCalculationFailureException;
import org.palladiosimulator.hwsimcoupling.util.CommandHandler;
import org.palladiosimulator.hwsimcoupling.util.FileManager;

public class FileManagerImpl implements FileManager {

	public FileManagerImpl() {
	}
	
	public Map<String, Serializable> copy_files(Map<String, Serializable> parameterMap, CommandHandler commandHandler) {
		Map<String, Serializable> stripped_parameter_map = new HashMap<String, Serializable>();
		for (Entry<String,Serializable> pair : parameterMap.entrySet()){
			stripped_parameter_map.put(pair.getKey(), copy_file(parameterMap, pair, commandHandler));
	    }
		return stripped_parameter_map;
	}
	
	private String copy_file(Map<String, Serializable> parameterMap, Entry<String, Serializable> pair, CommandHandler commandHandler) {
		String paths = pair.getValue().toString();
		String stripped_paths = "";
		for (String path : paths.split(" ")) {
			if (path.startsWith(LOCATIONS.ABSOLUTE.toString())) {
				stripped_paths += copy_absolute(parameterMap, path, commandHandler);
			} else if (path.startsWith(LOCATIONS.LOCAL.toString())) {
				stripped_paths += copy_local(parameterMap, path, commandHandler);
			} else {
				stripped_paths += path;
			}
		}
		return stripped_paths;
	}
	
	private String strip_path(String path) {
		if (path.startsWith(LOCATIONS.LOCAL.toString())) {
			return path.replaceFirst(LOCATIONS.LOCAL.toString(), "");
		} else if (path.startsWith(LOCATIONS.ABSOLUTE.toString())) {
			return path.replaceFirst(LOCATIONS.ABSOLUTE.toString(), "");
		} else {
			return path;
		}
	}
	
	private String copy_local(Map<String, Serializable> parameterMap, String path, CommandHandler commandHandler) {
		try {
			String stripped_path = strip_path(path);
			String project_name = stripped_path.split("/")[0];
			String project_path = ResourcesPlugin.getWorkspace().getRoot().getProject(project_name).getLocation().toString();
			String source_path = stripped_path.replace(project_name, project_path);
			CopyCommand copyCommand = commandHandler.getCopyCommand(parameterMap, source_path);
			CommandExecutor.execute_command(copyCommand, commandHandler.getOutputConsumer(), commandHandler.getErrorConsumer());
			return copyCommand.get_destination();
		} catch (IOException | InterruptedException e) {
			throw new DemandCalculationFailureException("Failed to copy file " + path + ": " + e.getMessage());
		}
	}
	
	private String copy_absolute(Map<String, Serializable> parameterMap, String path, CommandHandler commandHandler) {
		try {
			CopyCommand copyCommand = commandHandler.getCopyCommand(parameterMap, strip_path(path));
			CommandExecutor.execute_command(copyCommand, commandHandler.getOutputConsumer(), commandHandler.getErrorConsumer());
			return copyCommand.get_destination();
		} catch (IOException | InterruptedException e) {
			throw new DemandCalculationFailureException("Failed to copy file " + path + ": " + e.getMessage());
		}
	}

}
