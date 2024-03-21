package org.mentalizr.backend.exceptions;

public class M7rBusinessConstraintException extends Exception {

    public M7rBusinessConstraintException() {
    }

    public M7rBusinessConstraintException(String message) {
        super(message);
    }

    public M7rBusinessConstraintException(String message, Throwable cause) {
        super(message, cause);
    }

    public M7rBusinessConstraintException(Throwable cause) {
        super(cause);
    }

    public M7rBusinessConstraintException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
