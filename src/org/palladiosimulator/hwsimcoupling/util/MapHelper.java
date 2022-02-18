package org.palladiosimulator.hwsimcoupling.util;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import org.palladiosimulator.hwsimcoupling.exceptions.MissingParameterException;
import org.palladiosimulator.hwsimcoupling.util.FileManager.LOCATIONS;

public class MapHelper {
	
	public static final String[] excluded_keys = new String[] {"processingrate"};
	public static final String[] remove_prefixes = new String[] {LOCATIONS.ABSOLUTE.toString(), LOCATIONS.LOCAL.toString()};

	public static String get_required_value_from_map(Map<String, Serializable> map, String key) {
		if (map.get(key) != null) {
			return String.valueOf(map.get(key)).replaceAll("\"", "");
		} else {
			throw new MissingParameterException("The required parameter " + key + " is missing.");
		}
	}
	
	public static String get_value_from_map(Map<String, Serializable> map, String key) {
		if (map.get(key) != null) {
			return String.valueOf(map.get(key)).replaceAll("\"", "");
		} else {
			return null;
		}
	}
	
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
