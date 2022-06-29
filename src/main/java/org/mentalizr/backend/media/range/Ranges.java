package org.mentalizr.backend.media.range;

import de.arthurpicht.utils.core.assertion.AssertMethodPrecondition;

import java.util.ArrayList;
import java.util.List;

public class Ranges {

    private final List<Range> rangeList;

    public static Ranges getEmptyInstance() {
        return new Ranges(new ArrayList<>());
    }

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

    public boolean hasSingleRange() {
        return this.rangeList.size() == 1;
    }

    public boolean hasMultiRange() {
        return this.rangeList.size() > 1;
    }

    public int getNrOfRanges() {
        return this.rangeList.size();
    }

}
