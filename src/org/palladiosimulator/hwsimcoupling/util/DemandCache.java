package org.palladiosimulator.hwsimcoupling.util;

import java.io.Serializable;
import java.util.Map;

public interface DemandCache {

	public double get(Map<String, Serializable> parameterMap);
	
}
