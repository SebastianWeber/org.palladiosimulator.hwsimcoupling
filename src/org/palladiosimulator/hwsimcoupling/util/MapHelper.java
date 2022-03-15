package org.palladiosimulator.hwsimcoupling.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.palladiosimulator.hwsimcoupling.exceptions.MissingParameterException;

public class MapHelper {

    /**
     * Parameters with this key will be excluded in {@link MapHelper#getMapAsOneString(Map)}
     */
    public static final String[] EXCLUDEDKEYS = new String[] { "processingrate", "profile" };
    /**
     * The Value of parameters will be stripped from these prefixes in
     * {@link MapHelper#getMapAsOneString(Map)}
     */
    public static final String[] PREFIXESTOREMOVE = new String[] { Locations.ABSOLUTE.toString(),
            Locations.LOCAL.toString() };

    public static final String PAIRSEPARATOR = ":";
    public static final String PAIRSSEPARATOR = "|";

    /**
     * @param map
     * @param key
     * @return the value of the key
     * @throws MissingParameterException
     *             if the key is not found
     */
    public static String getRequiredValueFromMap(Map<String, Serializable> map, String key) {
        if (map.get(key) != null) {
            return String.valueOf(map.get(key))
                .replaceAll("\"", "");
        } else {
            throw new MissingParameterException("The required parameter " + key + " is missing.");
        }
    }

    /**
     * @param map
     * @param key
     * @return the value of the key or null if the key is not found
     */
    public static String getValueFromMap(Map<String, Serializable> map, String key) {
        if (map.get(key) != null) {
            return String.valueOf(map.get(key))
                .replaceAll("\"", "");
        } else {
            return null;
        }
    }

    /**
     * @param map
     * @return the map as one string without the parameters with keys in
     *         {@link MapHelper#EXCLUDEDKEYS} and values stripped from prefixes from
     *         {@link MapHelper#PREFIXESTOREMOVE}
     */
    public static String getMapAsOneString(Map<String, Serializable> map) {
        String oneString = "";
        List<String> keys = new ArrayList<String>(map.keySet());
        keys.sort(null);
        for (String key : keys) {
            oneString += getPairAsOneString(key, map.get(key)
                .toString());
        }
        return oneString;
    }

    private static String getPairAsOneString(String key, String value) {
        for (String excludedKey : EXCLUDEDKEYS) {
            if (excludedKey.equals(key)) {
                return "";
            }
        }
        for (String removePrefix : PREFIXESTOREMOVE) {
            if (value.startsWith(removePrefix)) {
                value = value.replaceFirst(removePrefix, "");
            }
        }
        return key + PAIRSEPARATOR + value + PAIRSSEPARATOR;
    }

}
