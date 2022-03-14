package org.palladiosimulator.hwsimcoupling.configuration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.palladiosimulator.hwsimcoupling.exceptions.DemandCalculationFailureException;
import org.palladiosimulator.hwsimcoupling.util.CommandHandler;

public class ProfileCache {

    private static ProfileCache instance;

    public static ProfileCache getInstance() {
        if (instance == null) {
            instance = new ProfileCache();
        }
        return instance;
    }

    private Map<String, Map<String, String>> profiles = PersistenceManager.loadProfiles();

    public void saveProfiles() {
        PersistenceManager.saveProfiles(profiles);
    }

    public void clearCache() {
        profiles.clear();
    }

    public Map<String, Map<String, String>> getProfiles() {
        return profiles;
    }

    public void addProfile(String key, Map<String, String> value) {
        profiles.put(key, value);
        saveProfiles();
    }

    public void removeProfile(String key) {
        profiles.remove(key);
        saveProfiles();
    }

    public Map<String, String> getRequiredParameters() {
        Map<String, String> requiredParameters = new HashMap<String, String>();
        for (Entry<String, CommandHandler> entry : ExtensionManager.getINSTANCE()
            .getCommandHandlers()
            .entrySet()) {
            requiredParameters.put(entry.getKey(), entry.getValue()
                .getParametersAsString());
        }
        return requiredParameters;
    }

    public Map<String, Serializable> mergeParameterMapWithProfile(Map<String, Serializable> parameterMap,
            String profile) {
        Map<String, Serializable> map = new HashMap<String, Serializable>();
        // Add the parameters from the profile first, so the parameter map can override them if
        // necessary
        map.putAll(getParameterMap(profile));
        map.putAll(parameterMap);
        return map;
    }

    public Map<String, String> getParameterMap(String profile) {
        if (profiles.get(profile) != null) {
            return profiles.get(profile);
        }
        throw new DemandCalculationFailureException("Could not find profile " + profile);
    }

    public String getProfile(String containerID) {
        for (Entry<String, Map<String, String>> profile : profiles.entrySet()) {
            if (profile.getValue()
                .get(Parameter.CONTAINERID.getKeyword())
                .equals(containerID)) {
                return profile.getKey();
            }
        }
        throw new DemandCalculationFailureException("Could not find profile with containerID " + containerID);
    }

}
