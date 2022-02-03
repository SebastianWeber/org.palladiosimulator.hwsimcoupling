package org.palladiosimulator.hwsimcoupling.consumers;

import java.util.function.Consumer;

public interface OutputConsumer extends Consumer<String>{

	public double get_demand();
	
}
