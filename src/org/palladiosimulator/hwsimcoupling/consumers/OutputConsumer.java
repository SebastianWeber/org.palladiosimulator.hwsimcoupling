package org.palladiosimulator.hwsimcoupling.consumers;

import java.util.function.Consumer;

/**
 * @author Sebastian A consumer that checks a stream for a demand and returns the value in
 *         {@link OutputConsumer#getDemand()}
 */
public interface OutputConsumer extends Consumer<String> {

    /**
     * @return the value of the demand found in the stream
     */
    public String getDemand();

}
