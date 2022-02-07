package org.palladiosimulator.hwsimcoupling.consumers;

import java.util.function.Consumer;

/**
 * @author Sebastian
 * A consumer that checks a stream for errors and reports them
 */
public interface ErrorConsumer extends Consumer<String>{

}
