package org.mentalizr.backend.rest.service;

public class ServicePreconditionFailedException extends Exception {

    public ServicePreconditionFailedException() {
    }

    public ServicePreconditionFailedException(String message) {
        super(message);
    }

    public ServicePreconditionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServicePreconditionFailedException(Throwable cause) {
        super(cause);
    }

    public ServicePreconditionFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
