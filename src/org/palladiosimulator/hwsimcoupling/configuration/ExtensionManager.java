package org.palladiosimulator.hwsimcoupling.configuration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.palladiosimulator.hwsimcoupling.exceptions.DemandCalculationFailureException;
import org.palladiosimulator.hwsimcoupling.util.CommandHandler;
import org.palladiosimulator.hwsimcoupling.util.MapHelper;

public class ExtensionManager {
	
	private static ExtensionManager INSTANCE;
	
	public static ExtensionManager getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new ExtensionManager();
		}
		return INSTANCE;
	}
	
	private ExtensionManager () {
		commandHandlers = loadCommandHandlers();
		profileCache = ProfileCache.getInstance();
	}

	private final String EXTENSION_POINT_ID = "org.palladiosimulator.hwsimcoupling.hwsim";
	private Map<String, CommandHandler> commandHandlers;
	private ProfileCache profileCache;
	
	public Map<String, CommandHandler> getCommandHandlers () {
		return commandHandlers;
	}
	
	public CommandHandler getCommandHandler(Map<String, Serializable> parameterMap) {
		String profile = MapHelper.get_value_from_map(parameterMap, Parameter.PROFILE.getKeyword());
		if (profile == null) {
			String containerID = MapHelper.get_required_value_from_map(parameterMap, Parameter.CONTAINERID.getKeyword());
			profile = profileCache.getProfile(containerID);
		}
		String hwsim = MapHelper.get_required_value_from_map(castMap(profileCache.getParameterMap(profile)), Parameter.HWSIM.getKeyword());
		CommandHandler commandHandler = commandHandlers.get(hwsim);		
		if (commandHandler != null) {
			return commandHandler;
		}
		throw new DemandCalculationFailureException("Failed to find plugin with extension name: " + hwsim);
	}
	
	private Map<String, Serializable> castMap(Map<String, String> mapStr) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		for (Entry<String, String> entry: mapStr.entrySet()) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}
	
	private Map<String, CommandHandler> loadCommandHandlers() {
		Map<String, CommandHandler> commandHandlers = new HashMap<String, CommandHandler>();
		for (IExtension extension : getExtensions()) {
			for (IConfigurationElement element : extension.getConfigurationElements()) {
				Object o;
				try {
					o = element.createExecutableExtension(Parameter.CLASS.getKeyword());
					if (o instanceof CommandHandler) {
						commandHandlers.put(element.getAttribute(Parameter.NAME.getKeyword()), (CommandHandler) o);
	                }
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
		return commandHandlers;
	}
	
	private IExtension[] getExtensions() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(EXTENSION_POINT_ID);
		if (point == null) {
			throw new DemandCalculationFailureException("Failed loading the extension point  " + EXTENSION_POINT_ID + ". Please install the plugin org.palladiosimulator.hwsimcoupling.");
		}
		return point.getExtensions();
	}
	
}
