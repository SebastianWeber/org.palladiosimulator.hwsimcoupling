package org.palladiosimulator.hwsimcoupling.exceptions;

/**
 * @author Sebastian Exception used to report a failure in the demand calculation
 */
public class DemandCalculationFailureException extends LoggingRuntimeException {

    protected static Class getOwningClass() {
        return MissingParameterException.class;
    }

    public DemandCalculationFailureException(String message) {
        super(message);
    }
}
