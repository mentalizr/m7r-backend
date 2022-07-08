package org.mentalizr.backend.media.range;

import java.util.ArrayList;
import java.util.List;

public class RangesBuilder {

    private final List<Range> rangeList;

    public RangesBuilder() {
        this.rangeList = new ArrayList<>();
    }

    public void addRange(Range range) {
        this.rangeList.add(range);
    }

    public Ranges build() {
        return new Ranges(this.rangeList);
    }

}
