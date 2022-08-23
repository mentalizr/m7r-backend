package org.mentalizr.backend.exceptions;

public class IllegalServiceInputException extends Exception {

    public IllegalServiceInputException() {
    }

    public IllegalServiceInputException(String message) {
        super(message);
    }

    public IllegalServiceInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalServiceInputException(Throwable cause) {
        super(cause);
    }

    public IllegalServiceInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
