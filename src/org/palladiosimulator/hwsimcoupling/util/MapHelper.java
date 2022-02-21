package org.palladiosimulator.hwsimcoupling.util;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import org.palladiosimulator.hwsimcoupling.exceptions.MissingParameterException;
import org.palladiosimulator.hwsimcoupling.util.FileManager.LOCATIONS;

public class MapHelper {
	
	/**
	 * Parameters with this key will be excluded in {@link MapHelper#get_map_as_one_string(Map)}
	 */
	public static final String[] excluded_keys = new String[] {"processingrate"};
	/**
	 * The Value of parameters will be stripped from these prefixes in {@link MapHelper#get_map_as_one_string(Map)}
	 */
	public static final String[] remove_prefixes = new String[] {LOCATIONS.ABSOLUTE.toString(), LOCATIONS.LOCAL.toString()};

	/**
	 * @param map
	 * @param key
	 * @return the value of the key
	 * @throws MissingParameterException if the key is not found
	 */
	public static String get_required_value_from_map(Map<String, Serializable> map, String key) {
		if (map.get(key) != null) {
			return String.valueOf(map.get(key)).replaceAll("\"", "");
		} else {
			throw new MissingParameterException("The required parameter " + key + " is missing.");
		}
	}
	
	/**
	 * @param map
	 * @param key
	 * @return the value of the key or null if the key is not found
	 */
	public static String get_value_from_map(Map<String, Serializable> map, String key) {
		if (map.get(key) != null) {
			return String.valueOf(map.get(key)).replaceAll("\"", "");
		} else {
			return null;
		}
	}
	
	/**
	 * @param map
	 * @return the map as one string without the parameters with keys in {@link MapHelper#excluded_keys} 
	 * and values stripped from prefixes from {@link MapHelper#remove_prefixes}
	 */
	public static String get_map_as_one_string(Map<String, Serializable> map) {
		String one_string = "";
		for (Entry<String,Serializable> pair : map.entrySet()){
			one_string += get_pair_as_one_string(pair);
	    }
		return one_string;
	}
	
	private static String get_pair_as_one_string(Entry<String,Serializable> pair) {
		String key = pair.getKey();
		String value = pair.getValue().toString();
		for (String excluded_key : excluded_keys) {
			if (excluded_key.equals(key)) {
				return "";
			}
		}
		for (String remove_prefix : remove_prefixes) {
			if (value.startsWith(remove_prefix)) {
				value = value.replaceFirst(remove_prefix, "");
			}
		}
		return key + ":" + value + "|";
	}
	
}
