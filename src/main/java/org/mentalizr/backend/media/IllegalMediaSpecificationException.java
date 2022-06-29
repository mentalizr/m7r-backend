package org.mentalizr.backend.media;

public class IllegalMediaSpecificationException extends BadRequestException {

    public IllegalMediaSpecificationException() {
        super("Illegal media specification.");
    }

}
