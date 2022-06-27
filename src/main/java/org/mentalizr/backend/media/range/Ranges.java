package org.mentalizr.backend.media.range;

import de.arthurpicht.utils.core.assertion.AssertMethodPrecondition;

import java.util.List;

public class Ranges {

    private final List<Range> rangeList;

    public Ranges(List<Range> rangeList) {
        AssertMethodPrecondition.parameterNotNull("rangeList", rangeList);
        this.rangeList = rangeList;
    }

    public List<Range> getRanges() {
        return this.rangeList;
    }

    public boolean hasRange() {
        return this.rangeList.size() > 0;
    }

    public boolean isSingleRange() {
        return this.rangeList.size() == 1;
    }

    public int size() {
        return this.rangeList.size();
    }

}
