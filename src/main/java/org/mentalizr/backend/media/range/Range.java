package org.mentalizr.backend.media.range;

public class Range {

    private final long begin;
    private final long end;
    private final long length;

    public Range(long begin, long end) {
        this.begin = begin;
        this.end = end;
        this.length = end - begin + 1;
    }

    public long getBegin() {
        return this.begin;
    }

    public long getEnd() {
        return this.end;
    }

    public long getLength() {
        return this.length;
    }

}
