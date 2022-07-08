package org.mentalizr.backend.media.range;

public class RangeHeaderOutOfBoundsException extends RangeHeaderParserException {

    public RangeHeaderOutOfBoundsException(String rangeValue) {
        super(rangeValue, "Range value out of bounds.");
    }

}
