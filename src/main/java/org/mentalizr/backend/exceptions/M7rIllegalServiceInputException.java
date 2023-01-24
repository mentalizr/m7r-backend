package org.mentalizr.backend.exceptions;

public class M7rIllegalServiceInputException extends Exception {

    private static final long serialVersionUID = -4812429961005802403L;

    public M7rIllegalServiceInputException() {
    }

    public M7rIllegalServiceInputException(String message) {
        super(message);
    }

    public M7rIllegalServiceInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public M7rIllegalServiceInputException(Throwable cause) {
        super(cause);
    }

    public M7rIllegalServiceInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
