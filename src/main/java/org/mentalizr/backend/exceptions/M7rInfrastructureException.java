package org.mentalizr.backend.exceptions;

public class M7rInfrastructureException extends Exception {

    private static final long serialVersionUID = 8704603286683503578L;

    public M7rInfrastructureException() {
    }

    public M7rInfrastructureException(String message) {
        super(message);
    }

    public M7rInfrastructureException(String message, Throwable cause) {
        super(message, cause);
    }

    public M7rInfrastructureException(Throwable cause) {
        super(cause);
    }

    public M7rInfrastructureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
