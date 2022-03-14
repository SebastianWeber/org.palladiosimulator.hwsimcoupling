package org.palladiosimulator.hwsimcoupling.exceptions;

/**
 * @author Sebastian Exception used to report a missing parameter
 */
public class MissingParameterException extends RuntimeException {

    public MissingParameterException(String message) {
        super(message);
    }

}
