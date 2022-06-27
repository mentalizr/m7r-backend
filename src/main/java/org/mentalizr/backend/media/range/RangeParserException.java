package org.mentalizr.backend.media.range;

public class RangeParserException extends Exception {

    public RangeParserException() {
    }

    public RangeParserException(String message) {
        super(message);
    }

    public RangeParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public RangeParserException(Throwable cause) {
        super(cause);
    }

    public RangeParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
