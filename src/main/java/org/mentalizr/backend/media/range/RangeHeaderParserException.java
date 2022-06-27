package org.mentalizr.backend.media.range;

public class RangeHeaderParserException extends RangeParserException {

    private static final String standardMessage = "Error on parsing http range header value: ";

    public RangeHeaderParserException(String rangeValue) {
        super(standardMessage + "[" + rangeValue + "].");
    }

    public RangeHeaderParserException(String rangeValue, String additionalMessage) {
        super(standardMessage + "[" + rangeValue + "]. " + additionalMessage);
    }

}
