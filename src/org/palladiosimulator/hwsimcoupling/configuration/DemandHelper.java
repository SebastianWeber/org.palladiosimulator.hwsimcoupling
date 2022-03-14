package org.palladiosimulator.hwsimcoupling.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.palladiosimulator.hwsimcoupling.exceptions.DemandCalculationFailureException;
import org.palladiosimulator.hwsimcoupling.util.DemandCache.RESOURCE;

public class DemandHelper {

    public static final String DEMANDSEPARATOR = ":";
    public static final String DEMANDSSEPARATOR = "&";
    public static final String DEMANDSTRUCTURE = "CPU" + DEMANDSEPARATOR + "<StoEx>" + DEMANDSSEPARATOR + "HDD"
            + DEMANDSEPARATOR + "<Stoex>";

    /**
     * Method to deserialize a given demand string to a map. This method does not parse any StoEx
     * given. The requirement for the keys to be a {@link RESOURCE} is not checked.
     * 
     * @param allDemands
     *            String with the format described in {@link DemandHelper#DEMANDSTRUCTURE}
     * @return Map containing the demands with corresponding {@link RESOURCE} as key
     */
    public static Map<String, String> deserializeDemand(String allDemands) {
        Map<String, String> demands = new HashMap<String, String>();
        for (String demand : allDemands.split(DEMANDSSEPARATOR)) {
            String[] demandSplit = demand.split(DEMANDSEPARATOR);
            if (demandSplit.length != 2) {
                throw new DemandCalculationFailureException(
                        "Failed to parse demand: " + demand + ". It should be \"" + DEMANDSTRUCTURE + "\".");
            }
            demands.put(demandSplit[0], demandSplit[1]);
        }
        return demands;
    }

    /**
     * Method to serialize a given demand map to a string.
     * 
     * @param allDemands
     *            Map containing the demands with corresponding {@link RESOURCE} as key
     * @return String with the format described in {@link DemandHelper#DEMANDSTRUCTURE}
     */
    public static String serializeDemand(Map<String, String> allDemands) {
        String demands = "";
        for (Entry<String, String> demand : allDemands.entrySet()) {
            demands += demand.getKey() + DEMANDSEPARATOR + demand.getValue() + DEMANDSSEPARATOR;
        }
        return demands.substring(0, demands.length() - 1);
    }

}
