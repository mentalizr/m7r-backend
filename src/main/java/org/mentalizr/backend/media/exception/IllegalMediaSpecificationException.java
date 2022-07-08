package org.mentalizr.backend.media.exception;

public class IllegalMediaSpecificationException extends BadRequestException {

    public IllegalMediaSpecificationException() {
        super("Illegal media specification.");
    }

}
