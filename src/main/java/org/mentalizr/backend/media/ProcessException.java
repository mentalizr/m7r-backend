package org.mentalizr.backend.media;

public class ProcessException extends Exception {

    private final int statusCode;

    public ProcessException(int statusCode, String responseMessage) {
        super(responseMessage);
        this.statusCode = statusCode;
    }

    public ProcessException(int statusCode, String responseMessage, Throwable cause) {
        super(responseMessage, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

}
