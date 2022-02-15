package org.palladiosimulator.hwsimcoupling.configuration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.palladiosimulator.hwsimcoupling.exceptions.DemandCalculationFailureException;
import org.palladiosimulator.hwsimcoupling.util.CommandHandler;
import org.palladiosimulator.hwsimcoupling.util.impl.DemandCacheImpl;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class HWsimCouplingManager {
	
	private static DemandCacheImpl SHAREDDEMANDCACHEIMPL;
	private static Map<String, Map<String, String>> currentProfiles = getParameters();
	
	private static Map<String, CommandHandler> extensionCommandHandlers = loadCommandHandlers();
	private static final String EXTENSION_POINT_ID =
            "org.palladiosimulator.hwsimcoupling.hwsim";
	
	private static Map<String, CommandHandler> loadCommandHandlers() {
		Map<String, CommandHandler> extensionCommandHandlers = new HashMap<String, CommandHandler>();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(EXTENSION_POINT_ID);
		if (point == null) {
			throw new DemandCalculationFailureException("Failed loading the extension point  " + EXTENSION_POINT_ID + ". Please install the plugin org.palladiosimulator.hwsimcoupling.");
		}
		IExtension[] extensions = point.getExtensions();
		for (IExtension extension : extensions) {
			for (IConfigurationElement element : extension.getConfigurationElements()) {
				Object o;
				try {
					o = element.createExecutableExtension("class");
					if (o instanceof CommandHandler) {
						extensionCommandHandlers.put(element.getAttribute("name"), (CommandHandler) o);
	                }
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
		return extensionCommandHandlers;
	}
	
	public static CommandHandler getCommandHandler(String extensionName) {
		
		CommandHandler commandHandler = extensionCommandHandlers.get(extensionName);		
		if (commandHandler != null) {
			return commandHandler;
		}
		throw new DemandCalculationFailureException("Failed to find plugin with extension name: " + extensionName);
	}
	
	public static Map<String, String> getRequiredParameters() {
		Map<String, String> requiredParameters = new HashMap<String, String>();
		for (Entry<String, CommandHandler> entry : extensionCommandHandlers.entrySet()) {
			requiredParameters.put(entry.getKey(), entry.getValue().getParametersAsString());
		}
		return requiredParameters;
	}
	
	public static Map<String, Serializable> mergeParameterMapWithProfile(Map<String, Serializable> parameterMap, String profile) {
		Map<String, Serializable> map =  new HashMap<String, Serializable>();
		map.putAll(getParameterMap(profile));
		map.putAll(parameterMap);
		return map;
	}
	
	public static Map<String, String> getParameterMap(String profile) {
		return currentProfiles.get(profile);
	}
	
	public static DemandCacheImpl getSharedDemandCacheImpl() {
		if (SHAREDDEMANDCACHEIMPL == null) {
			SHAREDDEMANDCACHEIMPL = new DemandCacheImpl();
		}
		return SHAREDDEMANDCACHEIMPL;
	}
	
	public static void saveParameters(Map<String, Map<String, String>> profiles) {
		Preferences preferences = InstanceScope.INSTANCE.getNode("org.palladiosimulator.hwsimcoupling");
		try {
			for (String children : preferences.childrenNames()) {
				preferences.node(children).removeNode();
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		for (Entry<String, Map<String, String>> profile : profiles.entrySet()) {
			Preferences node = preferences.node(profile.getKey());
			for (Entry<String, String> parameter : profile.getValue().entrySet()) {
				System.out.println(profile.getKey() + ": " + parameter.getKey() + " | " + parameter.getValue());
				node.put(parameter.getKey(), parameter.getValue());
			}
		}
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		currentProfiles = profiles;
	}
	
	public static Map<String, Map<String, String>> getParameters() {
		Preferences preferences = InstanceScope.INSTANCE.getNode("org.palladiosimulator.hwsimcoupling");
		Map<String, Map<String, String>> profiles = new HashMap<String, Map<String, String>>();
		String[] profileKeys = null;
		try {
			profileKeys = preferences.childrenNames();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		if (profileKeys == null || profileKeys.length == 0) {
			profileKeys = new String[] {"default"};
		}
		for (String profile : profileKeys) {
			Map<String, String> parameters = new HashMap<String, String>();
			profiles.put(profile, parameters);
			Preferences node = preferences.node(profile);
			String[] keys = new String[0];
			try {
				keys = node.keys();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
			for (String key : keys) {
				System.out.println(profile + ": " + key + " | " + node.get(key, "Failed loading"));
				parameters.put(key, node.get(key, "Failed loading"));
			}
			
		}
		currentProfiles = profiles;
		return profiles;
	}
	

}
