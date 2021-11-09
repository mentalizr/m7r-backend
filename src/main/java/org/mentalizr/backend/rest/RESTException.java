package org.mentalizr.backend.rest;

public class RESTException extends Exception {

    public RESTException() {
    }

    public RESTException(String message) {
        super(message);
    }

    public RESTException(String message, Throwable cause) {
        super(message, cause);
    }

    public RESTException(Throwable cause) {
        super(cause);
    }

    public RESTException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
