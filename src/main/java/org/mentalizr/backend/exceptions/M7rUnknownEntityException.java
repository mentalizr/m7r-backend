package org.mentalizr.backend.exceptions;

public class M7rUnknownEntityException extends Exception {

    private static final long serialVersionUID = -4812429961005802403L;

    public M7rUnknownEntityException() {
    }

    public M7rUnknownEntityException(String message) {
        super(message);
    }

    public M7rUnknownEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public M7rUnknownEntityException(Throwable cause) {
        super(cause);
    }

    public M7rUnknownEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
