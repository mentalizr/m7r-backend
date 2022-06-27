package org.mentalizr.backend.media.range;

public class Range {

    private final long begin;
    private final long end;

    public Range(long begin, long end) {
        this.begin = begin;
        this.end = end;
    }

    public long getBegin() {
        return begin;
    }

    public long getEnd() {
        return end;
    }

}
