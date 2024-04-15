package org.mentalizr.backend.exceptions;

import java.io.Serial;

public class M7rNoSuchResourceException extends Exception {

    @Serial
    private static final long serialVersionUID = 2810782882448776663L;

    public M7rNoSuchResourceException() {
    }

    public M7rNoSuchResourceException(String message) {
        super(message);
    }

    public M7rNoSuchResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public M7rNoSuchResourceException(Throwable cause) {
        super(cause);
    }

    public M7rNoSuchResourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
