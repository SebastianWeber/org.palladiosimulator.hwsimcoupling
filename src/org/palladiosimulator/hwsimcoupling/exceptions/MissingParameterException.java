package org.palladiosimulator.hwsimcoupling.exceptions;

/**
 * @author Sebastian Exception used to report a missing parameter
 */
public class MissingParameterException extends LoggingRuntimeException {

    protected static Class getOwningClass() {
        return MissingParameterException.class;
    }

    public MissingParameterException(String message) {
        super(message);
    }

}
