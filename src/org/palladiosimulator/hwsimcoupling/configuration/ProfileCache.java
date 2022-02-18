package org.palladiosimulator.hwsimcoupling.configuration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.palladiosimulator.hwsimcoupling.util.CommandHandler;
import org.palladiosimulator.hwsimcoupling.util.impl.DemandCacheImpl;

public class ProfileCache {

	private static ProfileCache INSTANCE;
	
	public static ProfileCache getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ProfileCache();
		}
		return INSTANCE;
	}
	
	private Map<String, Map<String, Serializable>> profiles = PersistenceManager.loadProfiles();	
	
	public void saveProfiles() {
		PersistenceManager.saveProfiles(profiles);
	}
	
	public Map<String, Map<String, Serializable>> getProfiles() {
		return profiles;
	}
	
	public void addProfile(String key, Map<String, Serializable> value) {
		profiles.put(key, value);
		saveProfiles();
	}
	
	public void removeProfile(String key) {
		profiles.remove(key);
		saveProfiles();
	}
	
	public Map<String, String> getRequiredParameters() {
		Map<String, String> requiredParameters = new HashMap<String, String>();
		for (Entry<String, CommandHandler> entry : ExtensionManager.getINSTANCE().getCommandHandlers().entrySet()) {
			requiredParameters.put(entry.getKey(), entry.getValue().getParametersAsString());
		}
		return requiredParameters;
	}
	
	public Map<String, Serializable> mergeParameterMapWithProfile(Map<String, Serializable> parameterMap, String profile) {
		Map<String, Serializable> map =  new HashMap<String, Serializable>();
		// Add the parameters from the profile first, so the parameter map can override them if necessary
		map.putAll(getParameterMap(profile));
		map.putAll(parameterMap);
		return map;
	}
	
	public Map<String, Serializable> getParameterMap(String profile) {
		return profiles.get(profile);
	}
	
	public String getProfile(String containerID) {
		for (Entry<String, Map<String, Serializable>> profile : profiles.entrySet()) {
			if (profile.getValue().get("containerID").equals(containerID)) {
				return profile.getKey();
			}
		}
		return null;
	}
	
}
