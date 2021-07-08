package org.mentalizr.backend.rest.entities;

public class ProgramConsistencyException extends Exception {

    public ProgramConsistencyException() {
    }

    public ProgramConsistencyException(String message) {
        super(message);
    }

    public ProgramConsistencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProgramConsistencyException(Throwable cause) {
        super(cause);
    }

    public ProgramConsistencyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
