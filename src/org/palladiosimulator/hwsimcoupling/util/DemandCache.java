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
	public double get(Map<String, Serializable> parameterMap);
	
}
