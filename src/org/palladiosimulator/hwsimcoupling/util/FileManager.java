package org.palladiosimulator.hwsimcoupling.util;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Sebastian
 * Copies one or multiple files to a container
 */
public interface FileManager {

	public String copy_file(Map<String, Serializable> parameterMap, Entry<String, Serializable> pair);
	
	public Map<String, Serializable> copy_files(Map<String, Serializable> parameterMap);
	
	/**
	 * @author Sebastian
	 * Paths processed by the {@link FileManager} are prefixed by
	 * "local:" means the path is relative to a project root with which it begins
	 * "absolute:" means the path is absolute, i.e. /home/user/... or C:/User/...
	 */
	public enum LOCATIONS {
		
		LOCAL("local:"), ABSOLUTE("absolute:");
		
		private final String keyword;
		
		private LOCATIONS(String keyword) {
			this.keyword = keyword;
		}
		
		public String toString() {
			return keyword;
		}
		
		public static String get_locations_string() {
			return "\"" + LOCATIONS.ABSOLUTE.toString() + "\" or \"" + LOCATIONS.LOCAL.toString() + "\"";
		}
		
	}
	
}
