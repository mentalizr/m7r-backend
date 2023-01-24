package org.mentalizr.backend.exceptions;

public class M7rInfrastructureRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 9046516293956084324L;

    public M7rInfrastructureRuntimeException() {
    }

    public M7rInfrastructureRuntimeException(String message) {
        super(message);
    }

    public M7rInfrastructureRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public M7rInfrastructureRuntimeException(Throwable cause) {
        super(cause);
    }

    public M7rInfrastructureRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
