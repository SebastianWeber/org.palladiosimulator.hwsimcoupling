package org.palladiosimulator.hwsimcoupling.consumers;

import java.util.function.Consumer;

/**
 * @author Sebastian A consumer that ignores the content of the stream
 */
public class VoidConsumer implements Consumer<String> {

    @Override
    public void accept(String t) {
        // No processing
    }

}
