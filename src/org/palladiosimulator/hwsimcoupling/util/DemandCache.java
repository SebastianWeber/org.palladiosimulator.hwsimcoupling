package org.palladiosimulator.hwsimcoupling.util;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Sebastian
 * Calculates demands and caches them
 */
public interface DemandCache {

	/**
	 * @param parameterMap
	 * @return the stored or calculated demand resulting from the given parameterMap
	 */
	public double get(Map<String, Serializable> parameterMap, RESOURCE resource, CommandHandler commandHandler);
	
	/**
	 * @author Sebastian
	 * The demand cache stores demands for cpu and hdd in one string
	 * separated by a semicolon and prefixed by keywords available through {@link RESOURCE#toString()}
	 */
	public enum RESOURCE {
		
		CPU("CPU:"), HDD("HDD:");
		
		private final String keyword;
		
		private RESOURCE(String keyword) {
			this.keyword = keyword;
		}
		
		public String toString() {
			return keyword;
		}

	}
	
}
