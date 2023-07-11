package org.mentalizr.backend.exceptions;

public class M7rInconsistencyException extends IllegalStateException {

    private static final long serialVersionUID = 7211166351549712730L;

    public M7rInconsistencyException() {
    }

    public M7rInconsistencyException(String s) {
        super(s);
    }

    public M7rInconsistencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public M7rInconsistencyException(Throwable cause) {
        super(cause);
    }

}
