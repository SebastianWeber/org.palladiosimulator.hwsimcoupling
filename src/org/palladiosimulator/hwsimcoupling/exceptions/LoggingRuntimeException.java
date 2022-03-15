package org.palladiosimulator.hwsimcoupling.exceptions;

import org.apache.log4j.Logger;

public class LoggingRuntimeException extends RuntimeException {

    protected static final Class<LoggingRuntimeException> ownningClass = getOwningClass();

    protected static final Logger LOGGER = org.apache.log4j.Logger.getLogger(ownningClass);

    protected static Class getOwningClass() {
        return LoggingRuntimeException.class;
    }

    public LoggingRuntimeException(String message) {
        super(message);
        LOGGER.error(message);
    }

}
