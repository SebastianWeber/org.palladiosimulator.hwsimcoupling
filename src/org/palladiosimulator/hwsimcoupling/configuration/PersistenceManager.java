package org.palladiosimulator.hwsimcoupling.configuration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class PersistenceManager {
	
	public static final String PREFERENCESID = "org.palladiosimulator.hwsimcoupling";
	public static final String PROFILENODEID = "profiles";
	public static final String DEMANDCACHENODEID = "demands";

	public static void saveProfiles(Map<String, Map<String, Serializable>> profiles) {
		Preferences preferences = InstanceScope.INSTANCE.getNode(PREFERENCESID).node(PROFILENODEID);
		try {
			for (String children : preferences.childrenNames()) {
				preferences.node(children).removeNode();
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		for (Entry<String, Map<String, Serializable>> profile : profiles.entrySet()) {
			Preferences node = preferences.node(profile.getKey());
			for (Entry<String, Serializable> parameter : profile.getValue().entrySet()) {
				node.put(parameter.getKey(), (String) parameter.getValue());
			}
		}
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String, Map<String, Serializable>> loadProfiles() {
		Preferences preferences = InstanceScope.INSTANCE.getNode(PREFERENCESID).node(PROFILENODEID);
		Map<String, Map<String, Serializable>> profiles = new HashMap<String, Map<String, Serializable>>();
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
			Map<String, Serializable> parameters = new HashMap<String, Serializable>();
			profiles.put(profile, parameters);
			Preferences node = preferences.node(profile);
			String[] keys = new String[0];
			try {
				keys = node.keys();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
			for (String key : keys) {
				parameters.put(key, node.get(key, "Failed loading"));
			}
			
		}
		return profiles;
	}
	
	public static void saveDemands(Map<String, String> demands) {
		try {
			Preferences preferences = InstanceScope.INSTANCE.getNode(PREFERENCESID).node(DEMANDCACHENODEID);
			preferences.clear();
			for (Entry<String, String> demand : demands.entrySet()) {
				preferences.put(demand.getKey(), demand.getValue());
			}
			preferences.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String, String> loadDemands() {
		Preferences preferences = InstanceScope.INSTANCE.getNode(PREFERENCESID).node(DEMANDCACHENODEID);
		Map<String, String> demands = new HashMap<String, String>();
		try {
			for (String key : preferences.keys()) {
				demands.put(key, preferences.get(key, "Failed loading"));		
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		return demands;
	}
	
}
